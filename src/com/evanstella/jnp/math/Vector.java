package com.evanstella.jnp.math;

import com.evanstella.jnp.core.IllegalDimensionException;
import com.evanstella.jnp.core.Numeric;


public final class Vector {

    // no instances for you
    private Vector ( ) {}


    /**************************************************************************
     * <p>Add vectors A and B. Both inputs must be vectors
     *
     * @param A the first vector
     * @param B the second vector
     *
     * @return a vector A+B
     *************************************************************************/
    public static Numeric add ( Numeric A, Numeric B ) {
        validateVectorFatal(A);
        validateVectorFatal(B);
        return Element.add( A, B );
    }

    /**************************************************************************
     * <p>Subtract vector B from A. Both inputs must be vectors
     *
     * @param A the first vector
     * @param B the second vector
     *
     * @return a vector A-B
     *************************************************************************/
    public static Numeric sub ( Numeric A, Numeric B ) {
        validateVectorFatal(A);
        validateVectorFatal(B);
        return Element.sub( A, B );
    }

    /**************************************************************************
     * <p>scale Vector A by B
     *
     * @param A the vector
     * @param B the scalar
     *
     * @return a vector A scaled by B
     *************************************************************************/
    public static Numeric scale ( Numeric A, Numeric B ) {
        validateVectorFatal(A);
        if ( !B.isScalar() )
            throw new IllegalDimensionException(
                "Vector Scaling: second argument must be a scalar"
            );
        return Element.mul( A, B );
    }

    /**************************************************************************
     * <p>Compute the magnitude of vector A. Note for complex valued vectors
     * the magnitude is defined as mag([z1,z2 ... zi]) = sqrt( sum( mag(zi) ) )
     *
     * @param A the vector to compute the magnitude for.
     *
     * @return a scalar magnitude(A)
     *************************************************************************/
    public static Numeric mag ( Numeric A ) {
        validateVectorFatal(A);
        double[] dataReal = A.getDataReal();
        double[] dataImag = A.getDataImag();
        boolean isComplex = dataImag != null;

        double re,im,mag = 0.0;
        for ( int i = 0; i < dataReal.length; i++ ) {
            re = dataReal[i];
            im = ( isComplex ) ? dataImag[i] : 0.0;
            mag += re*re + im*im;
        }

        return Numeric.Scalar(Math.sqrt(mag));
    }

    /**************************************************************************
     * <p>Compute the angle between vectors A and B in radians.
     *
     * @param A the first vector
     * @param B the second vector
     *
     * @return a scalar equal to the angle between A and B in radians
     *************************************************************************/
    public static Numeric angle ( Numeric A, Numeric B ) {
        validateVectorDimensions( A, B );
        Numeric magA = mag( A );
        Numeric magB = mag( B );
        Numeric cosine = Element.div( dot( A,B ), Element.mul(magA, magB) );
        double cos = cosine.valueReal();
        return Numeric.Scalar( Math.acos(cos) );
    }

    /**************************************************************************
     * <p>Compute the dot product of A and B
     *
     * @param A the first vector
     * @param B the second vector
     *
     * @return a scalar A.B
     *************************************************************************/
    public static Numeric dot ( Numeric A, Numeric B ) {
        validateVectorDimensions( A, B );
        return Element.sum( Element.mul(A, B) );
    }

    /**************************************************************************
     * <p>Compute the cross product of A and B. A and be must be vectors of
     * length 3.
     *
     * @param A the first vector
     * @param B the second vector
     *
     * @return a vector AxB
     *************************************************************************/
    public static Numeric cross ( Numeric A, Numeric B ) {
        validateVectorFatal(A);
        validateVectorFatal(B);
        double[] reA = A.getDataReal();
        double[] reB = B.getDataReal();
        double[] imA = A.getDataImag();
        double[] imB = B.getDataImag();
        if ( reA.length != 3 || reB.length != 3 )
            throw new IllegalDimensionException(
                "Cross product: both vectors must be of length 3."
            );
        if ( imA == null ) imA = new double[3];
        if ( imB == null ) imB = new double[3];
        double iReal,iImag,jReal,jImag,kReal,kImag;

        iReal = (reA[1]*reB[2]-imA[1]*imB[2]) - (reA[2]*reB[1]-imA[2]*imB[1]);
        iImag = (reA[1]*imB[2]+imA[1]*reB[2]) - (reA[2]*imB[1]+imA[2]*reB[1]);

        jReal = (reA[2]*reB[0]-imA[2]*imB[0]) - (reA[0]*reB[2]-imA[0]*imB[2]);
        jImag = (reA[2]*imB[0]+imA[2]*reB[0]) - (reA[0]*imB[2]+imA[0]*reB[2]);

        kReal = (reA[0]*reB[1]-imA[0]*imB[1]) - (reA[1]*reB[0]-imA[1]*imB[0]);
        kImag = (reA[0]*imB[1]+imA[0]*reB[1]) - (reA[1]*imB[0]+imA[1]*reB[0]);

        double[] realData = new double[]{ iReal, jReal, kReal };
        if ( iImag != 0 || jImag != 0 || kImag != 0 )
            return new Numeric( realData, new double[]{ iImag, jImag, kImag } );
        else
            return new Numeric( realData );
    }


    private static void validateVectorFatal (Numeric N) {
        if (N.isVector() )
            return;
        throw new IllegalDimensionException(
            "Vector operations: input must be a vector."
        );
    }

    private static void validateVectorDimensions (Numeric N1, Numeric N2) {
        validateVectorFatal(N1);
        validateVectorFatal(N2);

        if ( N1.getDataReal().length == N2.getDataReal().length)
            return;

        throw new IllegalDimensionException(
            "Vector operations: vectors must be the same length."
        );
    }

}