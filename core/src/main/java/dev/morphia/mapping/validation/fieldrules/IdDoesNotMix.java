package dev.morphia.mapping.validation.fieldrules;

import java.util.Set;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.codec.pojo.EntityModel;
import dev.morphia.mapping.codec.pojo.PropertyModel;
import dev.morphia.mapping.validation.ConstraintViolation;
import dev.morphia.mapping.validation.ConstraintViolation.Level;
import dev.morphia.sofia.Sofia;

/**
 * Checks that @Id is not with any other mapping annotation
 */
@SuppressWarnings("deprecation")
public class IdDoesNotMix extends PropertyConstraint {

    @Override
    protected void check(Mapper mapper, EntityModel entityModel, PropertyModel propertyModel, Set<ConstraintViolation> ve) {
        if (propertyModel.hasAnnotation(Id.class)
                && (propertyModel.hasAnnotation(Reference.class)
                        || propertyModel.hasAnnotation(Property.class))) {
            ve.add(new ConstraintViolation(Level.FATAL, entityModel, propertyModel, getClass(),
                    Sofia.invalidAnnotationCombination(propertyModel.getFullName(), Id.class.getSimpleName())));
        }
    }
}
