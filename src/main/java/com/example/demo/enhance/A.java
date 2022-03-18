//import java.lang.reflect.Field;
//
//public class PropertySetter {
//    public static void main(String[] args) {
//        // Create a new Person object
//        Person person = new Person("John Doe", 30);
//        // Display the person's name before modification
//        System.out.println("Before: " + person.getName());
//        // Change the person's name using the setPropertyName method
//        setPropertyName(person, "name", "Jane Doe");
//        // Display the person's name after modification
//        System.out.println("After: " + person.getName()); // Should output: Jane Doe
//    }
//
//    // Method to set the property of an object based on the property name and the value provided
//    public static void setPropertyName(Object obj, String propertyName, Object value) {
//        try {
//            // Retrieve the Field object for the specified property from the class of the provided object
//            Field field = obj.getClass().getDeclaredField(propertyName);
//            // Make the field accessible, even if it is private or protected
//            field.setAccessible(true);
//            // Check if the value is compatible with the field type before setting it
//            if (isCompatible(value, field.getType())) {
//                // Set the field's value for the given object to the specified value
//                field.set(obj, value);
//            } else {
//                // Throw an exception if the value is not compatible with the field type
//                throw new IllegalArgumentException("Incompatible value type provided.");
//            }
//        } catch (NoSuchFieldException e) {
//            // Handle case where the field does not exist in the object
//            System.out.println("Field not found: " + propertyName);
//        } catch (IllegalAccessException e) {
//            // Handle case where the field is inaccessible or final
//            System.out.println("Failed to access or modify the field: " + propertyName);
//        } catch (IllegalArgumentException e) {
//            // Handle other illegal argument issues, such as type incompatibility
//            System.out.println(e.getMessage());
//        }
//    }
//
//    // Helper method to check if the provided value is compatible with the field's type
//    private static boolean isCompatible(Object value, Class<?> type) {
//        // Check if value is null (null can be assigned to any non-primitive field)
//        if (value == null) return true;
//        // Check if the value instance matches the field's type or its wrapper class
//        return type.isInstance(value) || (type.isPrimitive() && wrap(type).isInstance(value));
//    }
//
//    // Method to wrap primitive types in their corresponding wrapper classes
//    private static Class<?> wrap(Class<?> type) {
//        // Return the wrapper class for int types
//        if (type == int.class) return Integer.class;
//            // Return the wrapper class for double types
//        else if (type == double.class) return Double.class;
//        // Add more primitives if necessary
//        // Return the original type if it's not a primitive needing wrapping
//        return type;
//    }
//
//    // Person class with private fields and a constructor
//    static class Person {
//        private String name;
//        private int age;
//
//        public Person(String name, int age) {
//            this.name = name;
//            this.age  = age;
//        }
//
//        // Getter method for the name field
//        public String getName() {
//            return name;
//        }
//    }
//}