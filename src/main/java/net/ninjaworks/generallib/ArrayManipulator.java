package net.ninjaworks.generallib;

import java.lang.reflect.Array;

/**
 * A general array manipulator.
 * You NEED to set the array that it manipulates, even after it has manipulated said array.
 * @param <T> The type that the manipulator should respond to
 */
@SuppressWarnings("all")
public class ArrayManipulator<T> {

    private T[] array;

    public ArrayManipulator() {
        this.array = null;
    }

    public ArrayManipulator(T[] array) {
        this.array = array;
    }

    /**
     * Joins the array with another.
     * @param array The array to join to
     * @param append Whether or not to append this array onto the given one ; if not,
     *               the given array will be appended onto this one
     * @return The joined arrays, null if it fails
     */
    public T[] joinToArray(T[] array, boolean append) {
        T[] newArray = null;
        if(this.array != null) {
            Class clazz = null;
            if(this.array.length > 0) {
                clazz = this.array[0].getClass();
            } else if(array.length > 0) {
                clazz = array[0].getClass();
            } else {
                return null;
            }
            if(clazz != null) {
                newArray = (T[]) Array.newInstance(clazz, this.array.length + array.length);
                T[] chosenFirstArray = array;
                T[] chosenSecondArray = this.array;
                if (append) {
                    chosenFirstArray = this.array;
                    chosenSecondArray = array;
                }
                for (int i = 0; i < chosenFirstArray.length; i++) {
                    newArray[i] = chosenFirstArray[i];
                }
                int extraCounter = 0;
                for (int i = chosenFirstArray.length; i < newArray.length; i++) {
                    newArray[i] = chosenSecondArray[extraCounter];
                    extraCounter++;
                }
            }
        }
        return newArray;
    }

    /**
     * Locks the manipulator onto an array.
     * THIS DOES NOT MODIFY THE ORIGINAL ARRAY,
     * so you will need to remember to set the manipulator top the modified array if you wish
     * for further manipulation.
     * @param array The array to set to
     */
    public void setArray(T[] array) {
        this.array = array;
    }

    /**
     * Pretty self-explanatory ; adds an element to the array. One of the most useful methods.
     * @param element The element to add
     * @return The modified array, null if it fails
     */
    public T[] addElement(T element) {
        T[] newArray = null;
        if(this.array != null) {
            newArray = (T[]) Array.newInstance(array.getClass().componentType(), array.length + 1);
            System.arraycopy(array, 0, newArray, 0, array.length);
            newArray[array.length] = element;
        }
        return newArray;
    }

    /**
     * Inserts an element at the index, displacing any following elements one step up.
     * @param element The element to insert
     * @param index The index at which to insert it
     * @return The modified array, null if it fails
     */
    public T[] insertElement(T element, int index) {
        if(this.array != null) {
            T[] newArray = (T[]) Array.newInstance(element.getClass(), array.length + 1);
            if(index >= 0) {
                for (int i = 0; i < index; i++) {
                    newArray[i] = this.array[i];
                }
                newArray[index] = element;
                for(int i = index + 1; i < array.length; i++) {
                    newArray[i + 1] = this.array[i];
                }
                return newArray;
            }
        }
        return this.array;
    }

    /**
     * Removes the element at the given index, displacing any following elements one step down.
     * @param index The index of the element to remove
     * @return The modified array, null if it fails
     */
    public T[] removeElementAt(int index) {
        if(this.array != null) {
            if(index > array.length - 1) {
                return this.array;
            }
            if(index < 0) {
                return this.array;
            }
            T element = this.array[index];
            T[] newArray = (T[]) Array.newInstance(element.getClass(), array.length - 1);
            if(index == 0) {
               System.arraycopy(array, 1, newArray, 0, array.length - 1);
            }
            for(int i = 0; i < index; i++) {
                newArray[i] = array[i];
            }
            for(int i = index; i < array.length - 1; i++) {
                newArray[i] = array[i + 1];
            }
            return newArray;
        } else {
            return null;
        }
    }

    /**
     * Finds out whether the array contains the given element.
     * @param element The element to look for
     * @return True if the element is present, false if not
     */
    public boolean arrayContainsElement(T element) {
        if(this.array != null) {
            for (T el : array) {
                if (el != null && el.equals(element))
                    return true;
            }
        }
        return false;
    }

    public int indexOf(T element) {
        if(this.array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(element)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Merges the array back into a string, probably useless.
     * @return The merged array, empty {@code String} if it fails
     */
    public String merge2String() {
        if(this.array != null) {
            if (this.array.length > 0) {
                StringBuilder builder = new StringBuilder(array[0].toString());
                for (T element : array) {
                    builder.append(element.toString());
                }
                return builder.toString();
            }
        }
        return "";
    }

    public String merge2String(char delimiter) {
        StringBuilder builder = new StringBuilder(array[0].toString());
        if(this.array != null) {
            if(this.array.length > 0) {
                for (int i = 0; i < array.length - 1; i++) {
                    builder.append(array[i].toString()).append(delimiter);
                }
                builder.append(array[array.length - 1]);
                return builder.toString();
            }
        }
        return "";
    }
}