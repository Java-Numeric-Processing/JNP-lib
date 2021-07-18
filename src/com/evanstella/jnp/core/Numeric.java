package com.evanstella.jnp.core;


public class Numeric extends NDArray {

    protected double[] dataReal;
    protected double[] dataImag;


    /**************************************************************************
     * Class constructor. Initializes a Numeric with parameterized dimensions.
     * The dimensions are deep copied to limit their write access to the object.
     *
     * @param dimensions The arbitrary dimensions for the N-D Numeric.
     *************************************************************************/
    public Numeric ( int ...dimensions ) {
        shape = new int[dimensions.length];
        System.arraycopy(dimensions, 0, shape,0, dimensions.length);
        int size = 1;
        for ( int n : dimensions )
            size *= n;
        dataReal = new double[size];
        dataImag = null;
    }

    /**************************************************************************
     * Class constructor. Initialize a Numeric from a double[]. Only a
     * shallow copy of the array is made
     *
     * @param indata The array to initialize from
     *************************************************************************/
    public Numeric ( double[] indata ) {
        dataReal = indata;
        shape = new int[]{ indata.length };
    }

    /**************************************************************************
     * Class constructor. Initialize a Numeric from a double[][]. Makes a
     * a deep copy in order to aggregate the double[][] into a double[].
     *
     * @param indata The array to initialize from
     *************************************************************************/
    public Numeric ( double[][] indata ) {
        dataReal = new double[indata.length * indata[0].length];
        int ind = 0;
        for ( double[] row : indata ) {
            if ( row.length != indata[0].length )
                throw new IllegalDimensionException(
                        "Input dimensions must be consistent."
                );
            for ( double d : row )
                dataReal[ind++] = d;
        }
        shape = new int[]{ indata.length, indata[0].length };
    }

    /**************************************************************************
     * Class constructor. Initialize a Numeric from a double[]. Only a
     * shallow copy of the array is made
     *
     * @param inDataReal The array to initialize the real data component from
     * @param inDataImag The array to initialize the imaginary data component
     *                   from
     *************************************************************************/
    public Numeric ( double[] inDataReal, double[] inDataImag ) {
        if ( inDataImag.length != inDataReal.length )
            throw new IllegalDimensionException(
                "Real and imaginary data must be the same size"
            );

        dataReal = inDataReal;
        dataImag = inDataImag;
        shape = new int[]{ inDataReal.length };
    }

    /**************************************************************************
     * Class constructor. Initialize a Numeric from a double[][]. Makes a
     * a deep copy in order to aggregate the double[][] into a double[].
     *
     * @param inDataReal The array to initialize the real data component from
     * @param inDataImag The array to initialize the imaginary data component
     *                   from
     *************************************************************************/
    public Numeric ( double[][] inDataReal, double[][] inDataImag ) {
        if ( inDataImag.length != inDataReal.length ||
             inDataImag[0].length != inDataReal[0].length)
            throw new IllegalDimensionException(
                "Real and imaginary data must be the same size"
            );
        dataReal = new double[inDataReal.length * inDataReal[0].length];
        dataImag = new double[inDataImag.length * inDataImag[0].length];
        int ind = 0;
        for ( int i = 0; i < inDataReal.length; i++ ) {
            if ( inDataReal[i].length != inDataReal[0].length ||
                 inDataImag[i].length != inDataImag[0].length )
                throw new IllegalDimensionException(
                    "Input dimensions must be consistent."
                );
            for ( int j = 0; j < inDataReal[i].length; i++ ) {
                dataReal[ind] = inDataReal[i][j];
                dataImag[ind] = inDataImag[i][j];
                ind++;
            }
        }
        shape = new int[]{ inDataReal.length, inDataReal[0].length };
    }

    /**************************************************************************
     * Initializes a Logical with all true values.
     *
     * @param dimensions The dimensions for the N-D Logical.
     *
     * @return a reference the initialized Logical
     *************************************************************************/
    public static Numeric Ones ( int ...dimensions ) {
        Numeric N = new Numeric( dimensions );
        java.util.Arrays.fill(N.dataReal, 1.0);
        return N;
    }

    /**************************************************************************
     * Initializes a Numeric with all zeros. Literally the same as
     * calling the constructor but here for completeness' sake.
     *
     * @param dimensions the dimensions for the N-D Numeric.
     *
     * @return a reference the initialized Numeric
     *************************************************************************/
    public static Numeric Zeros ( int ...dimensions ) {
        return new Numeric( dimensions );
    }

    /**************************************************************************
     * Initializes a Numeric with random real values using java.util.Random.
     *
     * @param dimensions the dimensions for the N-D Numeric.
     *
     * @return a reference the initialized Logical
     *************************************************************************/
    public static Numeric Rand ( int ...dimensions ) {
        java.util.Random R = new java.util.Random( );
        Numeric L = new Numeric( dimensions );

        for ( int i = 0; i < L.dataReal.length; i++ )
            L.dataReal[i] = R.nextDouble();

        return L;
    }

    /**************************************************************************
     * Initializes a Numeric with random real values using java.util.Random.
     *
     * @param dimensions the dimensions for the N-D Numeric.
     *
     * @return a reference the initialized Logical
     *************************************************************************/
    public static Numeric RandComplex ( int ...dimensions ) {
        java.util.Random R = new java.util.Random( );
        Numeric N = new Numeric( dimensions );

        for ( int i = 0; i < N.dataReal.length; i++ ) {
            N.dataReal[i] = R.nextDouble();
            N.dataImag[i] = R.nextDouble();
        }

        return N;
    }

    /**************************************************************************
     * Initializes a Numeric with random real values with a seed using
     * java.util.Random.
     *
     * @param seed       the PRNG seed.
     * @param dimensions the dimensions for the N-D Numeric.
     *
     * @return a reference the initialized Logical
     *************************************************************************/
    public static Numeric Rand ( long seed,  int ...dimensions ) {
        java.util.Random R = new java.util.Random( seed );
        Numeric L = new Numeric( dimensions );

        for ( int i = 0; i < L.dataReal.length; i++ )
            L.dataReal[i] = R.nextDouble();

        return L;
    }

    /**************************************************************************
     * Initializes a Numeric with random real values with a seed using
     * java.util.Random.
     *
     * @param seed       the PRNG seed.
     * @param dimensions the dimensions for the N-D Numeric.
     *
     * @return a reference the initialized Logical
     *************************************************************************/
    public static Numeric RandComplex ( long seed,  int ...dimensions ) {
        java.util.Random R = new java.util.Random( seed );
        Numeric N = new Numeric( dimensions );

        for ( int i = 0; i < N.dataReal.length; i++ ) {
            N.dataReal[i] = R.nextDouble();
            N.dataImag[i] = R.nextDouble();
        }

        return N;
    }

    /**************************************************************************
     * Gets a reference to the raw real data contained in the Numeric. Only
     * recommended if you need fast access to the data in the object.
     *
     * @return a reference to the Logical data.
     *************************************************************************/
    public double[] getDataReal ( ) {
        return dataReal;
    }

    /**************************************************************************
     * Gets a reference to the raw imaginary data contained in the Logical. Only
     * recommended if you need fast access to the data in the object. If the
     * Numeric contains only real numbers, the imaginary component is null.
     *
     * @return a reference to the Logical data.
     *************************************************************************/
    public double[] getDataImag ( ) {
        return dataImag;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value
     * WITHOUT resizing if the subscript is out of bounds of the data
     * dimensions. If this is the case, an IllegalDimensionException will be
     * thrown.
     *
     * @param inDataReal    the data to set (real component)
     * @param sub           the subscript at which to set the data
     *************************************************************************/
    public void set ( double inDataReal, int[] sub ) {
        int ind = sub2ind( shape, sub );
        if ( ind >= dataReal.length ) {
            throw new IllegalDimensionException(
                "Subscript out of bounds for dimensions"
            );
        }
        dataReal[ind] = inDataReal;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value
     * WITHOUT resizing if the subscript is out of bounds of the data
     * dimensions. If this is the case, an IllegalDimensionException will be
     * thrown.
     *
     * @param inDataReal    the data to set (real component)
     * @param inDataImag    the data to set (imaginary component)
     * @param sub           the subscript at which to set the data
     *************************************************************************/
    public void set ( double inDataReal, double inDataImag, int[] sub ) {
        int ind = sub2ind( shape, sub );
        if ( ind >= dataReal.length ) {
            throw new IllegalDimensionException(
                    "Subscript out of bounds for dimensions"
            );
        }
        dataReal[ind] = inDataReal;
        if ( dataImag == null )
            dataImag = new double[dataReal.length];
        dataImag[ind] = inDataImag;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value
     * WITHOUT resizing if the subscript is out of bounds of the data
     * dimensions.
     *
     * @param inDataReal    the data to set (real component)
     * @param ind           the index at which to set the data
     *************************************************************************/
    public void set ( double inDataReal, int ind ) {
        dataReal[ind] = inDataReal;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value
     * WITHOUT resizing if the subscript is out of bounds of the data
     * dimensions.
     *
     * @param inDataReal    the data to set (real component)
     * @param inDataImag    the data to set (imaginary component)
     * @param ind           the index at which to set the data
     *************************************************************************/
    public void set ( double inDataReal, double inDataImag, int ind ) {
        dataReal[ind] = inDataReal;
        if ( dataImag == null )
            dataImag = new double[dataReal.length];
        dataImag[ind] = inDataImag;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value. If
     * the subscript is out of bounds of the data dimensions, the data will be
     * resized. This can be slower on larger data sets as this
     * this operation is O(n) for data of size n.
     *
     * @param inDataReal    the data to set (real component)
     * @param sub           the subscript at which to set the data
     *************************************************************************/
    public void setAt ( double inDataReal, int[] sub ) {
        int ind = sub2ind( sub );
        if ( ind >= dataReal.length ) {
            int[] newDims = new int[shape.length];
            for ( int i = 0; i < shape.length; i++ ) {
                if ( shape[i] > sub[i] )
                    newDims[i] = shape[i];
                else
                    newDims[i] = sub[i] + 1;
            }
            resizeNoCheck( newDims );
            ind = sub2ind( sub );
        }
        dataReal[ind] = inDataReal;
    }

    /**************************************************************************
     * Sets the value of the data at the subscript with the inputted value. If
     * the subscript is out of bounds of the data dimensions, the data will be
     * resized. This can be slower on larger data sets as this
     * this operation is O(n) for data of size n.
     *
     * @param inDataReal    the data to set (real component)
     * @param inDataImag    the data to set (imaginary component)
     * @param sub           the subscript at which to set the data
     *************************************************************************/
    public void setAt ( double inDataReal, double inDataImag, int[] sub ) {
        int ind = sub2ind( sub );
        if ( ind >= dataReal.length ) {
            int[] newDims = new int[shape.length];
            for ( int i = 0; i < shape.length; i++ ) {
                if ( shape[i] > sub[i] )
                    newDims[i] = shape[i];
                else
                    newDims[i] = sub[i] + 1;
            }
            resizeNoCheck( newDims );
            ind = sub2ind( sub );
        }
        dataReal[ind] = inDataReal;
        if ( dataImag == null )
            dataImag = new double[dataReal.length];
        dataImag[ind] = inDataImag;
    }

    /**************************************************************************
     * Returns the real component of the data at the inputted subscript.
     *
     * @param sub   the subscript to retrieve data at
     *
     * @return the value at the subscript
     *************************************************************************/
    public double getReal ( int... sub ) {
        int ind = sub2ind( shape, sub );
        if ( ind >= dataReal.length )
            throw new IllegalDimensionException(
                "Subscript out of range of data dimensions");
        return dataReal[ind];
    }

    /**************************************************************************
     * Returns the imaginary component of the data at the inputted subscript.
     *
     * @param sub   the subscript to retrieve data at
     *
     * @return the value at the subscript
     *************************************************************************/
    public double getImag ( int... sub ) {
        if ( dataImag == null )
            return 0.0;
        int ind = sub2ind( shape, sub );
        if ( ind >= dataReal.length )
            throw new IllegalDimensionException(
                "Subscript out of range of data dimensions");
        return dataImag[ind];
    }

    /**************************************************************************
     * Returns the real component of the data at the inputted linear index.
     *
     * @param ind   the linear index to retrieve data at
     *
     * @return the value at the linear index
     *************************************************************************/
    public double getReal ( int ind ) {
        if ( ind >= dataReal.length )
            throw new IllegalDimensionException(
                "Index out of range of data size");
        return dataReal[ind];
    }

    /**************************************************************************
     * Returns the imaginary component of the data at the inputted linear index.
     *
     * @param ind   the linear index to retrieve data at
     *
     * @return the value at the linear index
     *************************************************************************/
    public double getImag ( int ind ) {
        if ( dataImag == null )
            return 0.0;
        if ( ind >= dataReal.length )
            throw new IllegalDimensionException(
                "Index out of range of data size");
        return dataReal[ind];
    }

    /**************************************************************************
     * TODO
     *************************************************************************/
    public int findMax ( ) {
        double max = dataReal[0];
        int ind = 0;
        for ( int i = 0; i < dataReal.length; i++ ) {
            if ( dataReal[i] > max) {
                max = dataReal[i];
                ind = i;
            }
        }
        return ind;
    }

    /**************************************************************************
     * TODO
     *************************************************************************/
    public int findAbsMax ( ) {
        double max = dataReal[0];
        double absVal, tmp;
        int ind = 0;
        for ( int i = 0; i < dataReal.length; i++ ) {
            tmp = dataReal[i];
            absVal = (tmp <= 0.0) ? 0.0 - tmp : tmp;
            if ( absVal > max) {
                max = absVal;
                ind = i;
            }
        }
        return ind;
    }

    /**************************************************************************
     * Reshapes the Numeric by simply changing the dimensions, not the data.
     * This means the output will still be in row-major order and the dimensions
     * must satisfy the requirement that the number of elements must not change.
     *
     * @param dimensions    The dimensions to shape the data to
     *
     * @return a reference to the reshaped Numeric
     *************************************************************************/
    public Numeric reshape ( int ...dimensions ) {
        int size = 1;
        for ( int n : dimensions )
            size *= n;
        if ( size != dataReal.length )
            throw new IllegalDimensionException(
                "Invalid Dimensions. Number of elements must be the same"
            );
        Numeric reshaped = new Numeric( dimensions );
        copyData( reshaped );
        return reshaped;
    }

    /**************************************************************************
     * Transposes the Numeric. Only defined for Matrices (<3 dimensions); any
     * input of a higher dimension than 2 will cause an exception
     *
     * @return a reference to the transposed data.
     *************************************************************************/
    public Numeric transpose ( ) {
        if ( shape.length > 2 )
            throw new IllegalDimensionException(
                "Transpose operation is not defined for data with more than 2 dimensions"
            );

        Numeric transposed;
        if ( shape.length == 1 )
            transposed = new Numeric( shape[0], 1 );
        else
            transposed = new Numeric( shape[1], shape[0] );

        int r = transposed.shape[0];
        int c = transposed.shape[1];
        for ( int i = 0; i < r; i++ )
            for (int j = 0; j < c; j++)
                transposed.dataReal[i*c+j] = dataReal[j*r+i];
        if ( dataImag != null ) {
            transposed.dataImag = new double[transposed.dataReal.length];
            for ( int i = 0; i < r; i++ )
                for (int j = 0; j < c; j++)
                    transposed.dataImag[i*c+j] = dataImag[j*r+i];
        }

        return transposed;
    }

    /**************************************************************************
     * flattens the Numeric to one dimension without changing any of the data.
     *
     * @return a reference to the flattened Numeric
     *************************************************************************/
    public Numeric flatten ( ) {
        Numeric flat = new Numeric( 1, dataReal.length );
        copyData( flat );
        return flat;
    }

    /**************************************************************************
     * Creates a deep copy of this Numeric object and returns a reference to
     * the copy.
     *
     * @return a reference to the copied object
     *************************************************************************/
    public Numeric copy ( ) {
        Numeric copy = new Numeric( this.shape );
        copyData( copy );
        return copy;
    }

    /**************************************************************************
     * Returns a Numeric with all dimensions of length 1 removed. If the
     * resulting data is one dimensional, the resulting object will be a row
     * vector (1xN) if the first dimension (row) was length 1, and a column
     * vector (Nx1) otherwise.
     *
     * @return a reference to the resulting Numeric.
     *************************************************************************/
    public Numeric squeeze ( ) {
        int newSize = 0;
        for ( int n : shape ) {
            if (n > 1) newSize++;
        }
        boolean makeColumn = false;
        if ( shape[0] != 1 && newSize == 1 ) {
            makeColumn = true;
            newSize++;
        }
        int[] newShape = new int[newSize];
        int ind = 0;
        for ( int n : shape ) {
            if (n > 1) newShape[ind++] = n;
        }
        if ( makeColumn )
            newShape[ind] = 1;
        Numeric squeezed = new Numeric( newShape );
        copyData( squeezed );
        return squeezed;
    }

    /**************************************************************************
     * Resizes the array containing the Numeric data while keeping data in its
     * current subscripted position.
     *
     * Should be done infrequently as this requires copying each element to a
     * new array. There is also a little overhead associated with calculating
     * the new element indices. Elements in newly allocated space are
     * initialized to 0.0
     *
     * @param dimensions The new dimensions for the data
     *************************************************************************/
    public void resize ( int ...dimensions ) {
        for ( int i = 0; i < shape.length; i++ )
            if ( shape[i] > dimensions[i] )
                throw new IllegalDimensionException(
                        "Resized dimensions must be greater than current dimensions"
                );

        int newSize = 1;
        for ( int n : dimensions )
            newSize *= n;

        boolean resizeImag = dataImag != null;
        double[] newDataReal = new double[newSize];
        double[] newDataImag = null;
        if ( resizeImag ) newDataImag = new double[newSize];

        int[] sub;
        int newInd;
        for ( int i = 0; i < dataReal.length; i++ ) {
            sub = ind2sub( shape, i );
            newInd = sub2ind( dimensions, sub );
            newDataReal[newInd] = dataReal[i];
            if ( resizeImag )
                newDataImag[newInd] = dataImag[i];
        }
        dataReal = newDataReal;
        dataImag = newDataImag;
        shape = dimensions;
    }

    /**************************************************************************
     * Slices the data into a sub Numeric
     *
     * @param dimensions    An int[] for each dimension, e.i. if the data is 3
     *                      dimensional, inputs would be int[],int[],int[]
     *                      (or int[3][]). Each int[] should have either one
     *                      value (int[]{n}) specifying the nth index of the
     *                      dimension, or two values specifying a range of
     *                      values (int[]{0,4}: inclusive, exclusive).
     *
     *                      Example: for a 5x5 matrix:
     *                      slice(new int{0,5}, new int{2}) returns the 3rd
     *                      column of the matrix.
     *
     * @return a reference to the sliced Numeric.
     *************************************************************************/
    public Numeric slice ( int[] ...dimensions ) {
        if ( dimensions.length != shape.length )
            throw new IllegalDimensionException(
                "Number of slice dimensions must match number of data dimensions");
        int[] newShape = new int[shape.length];
        int size = 1;
        for ( int i = 0; i < shape.length; i++ ) {
            int[] dim = dimensions[i];
            if ( dim.length == 1 )
                newShape[i] = 1;
            else if ( dim.length == 2 ) {
                if ( dim[1] > shape[i] )
                    throw new IllegalDimensionException(
                        "Slice index " + dim[1] + " out of bounds of dimension " + i);
                if ( dim[0] >= dim[1] )
                    throw new IllegalDimensionException(
                        "Slice index 1 must be greater than index 2 for dimension " + i);
                newShape[i] = dim[1] - dim[0];
                size *= (dim[1] - dim[0]);
            }
            else
                throw new IllegalDimensionException(
                    "Slice indices must be a single index or two values");
            if ( newShape[i] < 1 )
                throw new IllegalDimensionException(
                    "Slice indices must be positive");
        }

        double[][] newData = sliceData( dimensions, newShape, size );

        Numeric sliced = new Numeric( newShape );
        sliced.dataReal = newData[0];
        sliced.dataImag = newData[1];
        return sliced;
    }

    /**************************************************************************
     * Concatenate the inputted Numeric to the end of this Numeric. Note that
     * the number of dimensions and the dimension lengths, except for the
     * dimension being concatenated along, must be equal.
     *
     * @param dimension the dimension to concatenate along
     * @param N         the Numeric to concatenate
     *
     * @return a reference to the new Numeric
     *************************************************************************/
    public Numeric concat ( int dimension, Numeric N ) {
        if ( dimension >= shape.length )
            throw new IllegalDimensionException(
                "Logical does not have " + (dimension+1) + " dimensions.");
        if ( shape.length != N.shape.length )
            throw new IllegalDimensionException(
                "Both objects must have the same number of dimensions.");
        for ( int i = 0; i < shape.length; i++ ) {
            if ( i != dimension && shape[i] != N.shape[i] )
                throw new IllegalDimensionException(
                "All dimensions but the concat dimension must be equal in length.");
        }

        Numeric catted = this.copy( );
        int[] newShape = catted.shape( );
        newShape[dimension] = shape[dimension] + N.shape[dimension];
        catted.resizeNoCheck( newShape );

        boolean catImag = dataImag != null;
        if ( catImag ) catted.dataImag = new double[catted.dataReal.length];
        int[] sub;
        int ind;
        for ( int i = 0; i < N.dataReal.length; i++ ) {
            sub = ind2subNoCheck( N.shape, i );
            sub[dimension] += shape[dimension];
            ind = sub2indNoCheck( newShape, sub );
            catted.dataReal[ind] = N.dataReal[i];
            if ( catImag ) catted.dataImag[ind] = N.dataImag[i];
        }

        return catted;
    }

    /**************************************************************************
     * Creates a string representation of the Logical for printing. Will only
     * show actual data for 1 and 2 dimensional data as higher dimensional
     *
     *
     * data is difficult to display well in a string.
     *
     * @return a string representation of the Logical
     *************************************************************************/
    public String toString ( ) {
        boolean isComplex = dataImag != null;
        int maxInd = findAbsMax( );
        double max = dataReal[maxInd];
        int L = String.format( "%.2f", max ).length();

        StringBuilder s = new StringBuilder(super.toString() + " <Numeric>\n");
        if ( shape.length < 3 ) {
            int row, col;
            if ( shape.length == 2 ) {
                row = shape[0];
                col = shape[1];
            } else {
                row = 1;
                col = shape[0];
            }
            for ( int i = 0; i < row; i++ ) {
                if ( dataReal[i * col] < 0 )
                    if ( isComplex )
                        s.append("[  ");
                    else
                        s.append("[ ");
                else
                    if ( isComplex )
                        s.append("[   ");
                    else
                        s.append("[  ");
                for ( int j = 0; j < col; j++ ) {
                    String tmp;
                    double real = dataReal[i * col + j];
                    if ( real < 0 )
                        tmp = String.format("%" + (L+1) + ".2f", dataReal[i * col + j]);
                    else
                        tmp = String.format("%" + L + ".2f", dataReal[i * col + j]);
                    if ( isComplex ) {
                        double img = dataImag[i * col + j];
                        if ( img < 0 )
                            tmp = tmp + " - " + String.format("%" + L + ".2fi", img);
                        else
                            tmp = tmp + " + " + String.format("%" + L + ".2fi", img);
                    }
                    if ( j < col-1 && dataReal[i * col + j+1] < 0 )
                        if ( isComplex )
                            s.append(tmp).append("  ");
                        else
                            s.append(tmp).append(" ");
                    else
                        if ( isComplex )
                            s.append(tmp).append("   ");
                        else
                            s.append(tmp).append("  ");
                }
                s.append("]\n");
            }
        }

        return s.toString();
    }

    /**************************************************************************
     * Compares two Logical objects to check if they are equal in both
     * dimension and data.
     *
     * @param N the other Logical to compare this one to
     *
     * @return  true if the two Logical objects are equal, false otherwise.
     *************************************************************************/
    public boolean equals ( Numeric N ) {
        if ( this == N )
            return true;
        if ( N.shape.length != this.shape.length )
            return false;
        for ( int i = 0; i < shape.length; i++ ) {
            if ( this.shape[i] != N.shape[i] )
                return false;
        }
        boolean isComplex = dataImag != null;
        if ( isComplex && N.dataImag == null )
            return false;
        else if ( !isComplex && N.dataImag != null )
            return false;

        for ( int i = 0; i < dataReal.length; i++ ) {
            if ( this.dataReal[i] != N.dataReal[i] )
                return false;
            if ( isComplex && this.dataImag[i] != N.dataImag[i] )
                return false;
        }

        return true;
    }

    /**************************************************************************
     * TODO
     *************************************************************************/
    public boolean equalsTolerance ( Numeric N, double tolerance ) {
        if ( this == N )
            return true;
        if ( N.shape.length != this.shape.length )
            return false;
        for ( int i = 0; i < shape.length; i++ ) {
            if ( this.shape[i] != N.shape[i] )
                return false;
        }
        boolean isComplex = dataImag != null;
        if ( isComplex && N.dataImag == null )
            return false;
        else if ( !isComplex && N.dataImag != null )
            return false;

        for ( int i = 0; i < dataReal.length; i++ ) {
            if ( this.dataReal[i] != N.dataReal[i] )
                return false;
            if ( isComplex && this.dataImag[i] != N.dataImag[i] )
                return false;
        }

        return true;
    }


    /**************************************************************************
     *                          Internal Methods
     *************************************************************************/

    /* Resize data without checking dimensions. */
    protected void resizeNoCheck ( int ...dimensions ) {
        int newSize = 1;
        for ( int n : dimensions )
            newSize *= n;

        boolean resizeImag = dataImag != null;
        double[] newDataReal = new double[newSize];
        double[] newDataImag = null;
        if ( resizeImag ) newDataImag = new double[newSize];

        int[] sub;
        int newInd;
        for ( int i = 0; i < dataReal.length; i++ ) {
            sub = ind2sub( shape, i );
            newInd = sub2ind( dimensions, sub );
            newDataReal[newInd] = dataReal[i];
            if ( resizeImag )
                newDataImag[newInd] = dataImag[i];
        }
        dataReal = newDataReal;
        dataImag = newDataImag;
        shape = dimensions;
    }

    /* Moved this to its own function bc slice was getting a little long */
    protected double[][] sliceData ( int[][] dims, int[] newShape, int size ) {
        // get first subscript in the slice
        int[] sub = new int[shape.length];
        int ind = 0;
        for ( int[] dim : dims )
            sub[ind++] = dim[0];

        // get the last subscript in the slice
        int[] subLast = new int[shape.length];
        ind = 0;
        for ( int[] dim : dims ) {
            if ( dim.length == 1 )
                subLast[ind++] = dim[0];
            else
                subLast[ind++] = dim[1]-1;
        }

        boolean sliceImag = dataImag != null;

        // start at first subscript, get all elements that fall in the slice
        double[] newDataReal = new double[size];
        double[] newDataImag = null;
        if ( sliceImag ) newDataImag = new double[size];
        int startInd = sub2indNoCheck( shape, sub );
        int endInd = sub2indNoCheck( shape, subLast );

        ind = 0;
        newDataReal[ind] = dataReal[startInd];
        if ( sliceImag ) newDataImag[ind] = dataImag[startInd];
        ind++;

        for ( int i = startInd+1; i < endInd; i++ ) {
            sub = ind2subNoCheck( shape, i );
            boolean add = true;
            for ( int j = 0; j < shape.length; j++ ) {
                if ( dims[j].length == 1 && sub[j] != dims[j][0] ) {
                    add = false;
                    break;
                } else if ( dims[j].length == 2 &&
                        (sub[j] < dims[j][0] || sub[j] >= dims[j][1]) ) {
                    add = false;
                    break;
                }
            }
            if ( add ) {
                newDataReal[ind] = dataReal[i];
                if ( sliceImag ) newDataImag[ind] = dataImag[i];
                ind++;
            }
        }

        if ( startInd != endInd ) {
            newDataReal[ind] = dataReal[endInd];
            if ( sliceImag ) newDataImag[ind] = dataImag[endInd];
        }

        return new double[][]{ newDataReal, newDataImag };
    }

    /* TODO */
    protected void copyData ( Numeric dest ) {
        System.arraycopy(
            dataReal, 0, dest.dataReal, 0, dataReal.length );
        if ( dataImag != null ) {
            dest.dataImag = new double[dest.dataReal.length];
            System.arraycopy(
                dataImag, 0, dest.dataImag, 0, dataImag.length );
        }
    }

}
