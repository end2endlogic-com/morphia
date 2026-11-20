package dev.morphia.test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.morphia.MorphiaDatastore;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.config.ManualMorphiaConfig;
import dev.morphia.config.MorphiaConfig;
import dev.morphia.mapping.MappingException;
import dev.morphia.mapping.codec.pojo.EntityModel;
import dev.morphia.test.models.Author;
import dev.morphia.test.models.Book;
import dev.morphia.test.models.User;

import org.bson.types.ObjectId;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

/**
 * A datastore built from entity types discovered ahead of time maps exactly those types and does not fall back to scanning the
 * configured packages.
 */
public class TestPreDiscoveredEntities extends TestBase {
    private static final String DATABASE = "morphia_prediscovered";

    @Entity("prediscovered_indexed")
    public static class IndexedThing {
        @Id
        private ObjectId id;
        @Indexed
        private String name;
    }

    private static MorphiaConfig config() {
        // The package holds many more entities than the ones handed in below. If the datastore scanned it, they would be mapped.
        return ManualMorphiaConfig.configure()
                .database(DATABASE)
                .packages(List.of(User.class.getPackageName()));
    }

    private static Set<Class<?>> mappedTypes(MorphiaDatastore datastore) {
        return datastore.getMapper().getMappedEntities().stream()
                .map(EntityModel::getType)
                .collect(Collectors.toSet());
    }

    private List<String> indexNames(String collection) {
        return getMongoClient().getDatabase(DATABASE).getCollection(collection)
                .listIndexes().into(new java.util.ArrayList<>()).stream()
                .map(index -> index.getString("name"))
                .collect(Collectors.toList());
    }

    @Test
    public void mapsExactlyTheGivenTypes() {
        MorphiaConfig config = config();
        MorphiaDatastore datastore = new MorphiaDatastore(getMongoClient(), config, List.of(Author.class, Book.class));

        assertEquals(mappedTypes(datastore), Set.of(Author.class, Book.class));
        assertFalse(datastore.getMapper().isMapped(User.class), "a type from the configured package was mapped without being handed in");
        assertEquals(datastore.getMapper().getConfig().packages(), config.packages(), "the configured packages must be left as they are");
    }

    @Test
    public void emptyListMapsNothing() {
        MorphiaDatastore datastore = new MorphiaDatastore(getMongoClient(), config(), List.of());

        assertTrue(mappedTypes(datastore).isEmpty());
    }

    @Test
    public void typesLeftOutCanStillBeMappedOnDemand() {
        MorphiaDatastore datastore = new MorphiaDatastore(getMongoClient(), config(), List.of(Author.class));

        assertEquals(datastore.getMapper().getEntityModel(User.class).getType(), User.class);
        assertTrue(datastore.getMapper().isMapped(User.class));
    }

    @Test
    public void rejectsTypeWithoutMappingAnnotation() {
        assertThrows(MappingException.class,
                () -> new MorphiaDatastore(getMongoClient(), config(), List.of(Author.class, String.class)));
    }

    @Test
    public void appliesIndexesWhenConfigSaysSo() {
        getMongoClient().getDatabase(DATABASE).drop();

        new MorphiaDatastore(getMongoClient(), config().applyIndexes(false), List.of(IndexedThing.class));
        assertFalse(indexNames("prediscovered_indexed").contains("name_1"), "indexes were applied although the config turns them off");

        new MorphiaDatastore(getMongoClient(), config().applyIndexes(true), List.of(IndexedThing.class));
        assertTrue(indexNames("prediscovered_indexed").contains("name_1"), "indexes were not applied although the config turns them on");

        getMongoClient().getDatabase(DATABASE).drop();
    }

    @Test
    public void packageConstructorStillScans() {
        MorphiaDatastore datastore = new MorphiaDatastore(getMongoClient(), config());

        Set<Class<?>> mapped = mappedTypes(datastore);
        assertTrue(mapped.contains(User.class));
        assertTrue(mapped.contains(Author.class));
    }
}
