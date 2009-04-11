package net.java.dev.aircarrier.model.XMLparser;

import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.WireframeState;
import com.jme.util.TextureManager;

import java.net.URL;

class Jme1Helper {

    /**
     * When used in texture loading, this indicates to let jME guess
     * the format, but not to use S3TC compression, even if available.
     */
    public static final int Image_GUESS_FORMAT_NO_S3TC = -2;
    /**
     * When used in texture loading, this indicates to let jME guess
     * the format.  jME will use S3TC compression, if available.
     */
    public static final int Image_GUESS_FORMAT = -1;


    // SOURCE FUNCTIONS
    public final static int SB_ZERO = 0;
    public final static int SB_ONE = 1;
    public final static int SB_DST_COLOR = 2;
    public final static int SB_ONE_MINUS_DST_COLOR = 3;
    public final static int SB_SRC_ALPHA = 4;
    public final static int SB_ONE_MINUS_SRC_ALPHA = 5;
    public final static int SB_DST_ALPHA = 6;
    public final static int SB_ONE_MINUS_DST_ALPHA = 7;
    public final static int SB_SRC_ALPHA_SATURATE = 8;

    static BlendState.SourceFunction getBlendSourceFunction(Integer i) {
        switch(i) {
            /**
             * The source value of the blend function is all zeros.
             */
            case SB_ZERO:
                return BlendState.SourceFunction.Zero;
            /**
             * The source value of the blend function is all ones.
             */
            case SB_ONE:
                return BlendState.SourceFunction.One;
            /**
             * The source value of the blend function is the destination color.
             */
            case SB_DST_COLOR:
                return BlendState.SourceFunction.DestinationColor;
            /**
             * The source value of the blend function is 1 - the destination color.
             */
            case SB_ONE_MINUS_DST_COLOR:
                return BlendState.SourceFunction.OneMinusDestinationColor;
            /**
             * The source value of the blend function is the source alpha value.
             */
            case SB_SRC_ALPHA:
                return BlendState.SourceFunction.SourceAlpha;
            /**
             * The source value of the blend function is 1 - the source alpha value.
             */
            case SB_ONE_MINUS_SRC_ALPHA:
                return BlendState.SourceFunction.OneMinusSourceAlpha;
            /**
             * The source value of the blend function is the destination alpha.
             */
            case SB_DST_ALPHA:
                return BlendState.SourceFunction.DestinationAlpha;
            /**
             * The source value of the blend function is 1 - the destination alpha.
             */
            case SB_ONE_MINUS_DST_ALPHA:
                return BlendState.SourceFunction.OneMinusDestinationAlpha;
            /**
             * The source value of the blend function is the minimum of alpha or 1 - alpha.
             */
            case SB_SRC_ALPHA_SATURATE:
                return BlendState.SourceFunction.SourceAlphaSaturate;
        }
        return null;
    }


    //destination functions
    public final static int DB_ZERO = 0;
    public final static int DB_ONE = 1;
    public final static int DB_SRC_COLOR = 2;
    public final static int DB_ONE_MINUS_SRC_COLOR = 3;
    public final static int DB_SRC_ALPHA = 4;
    public final static int DB_ONE_MINUS_SRC_ALPHA = 5;
    public final static int DB_DST_ALPHA = 6;
    public final static int DB_ONE_MINUS_DST_ALPHA = 7;

    static BlendState.DestinationFunction getBlendDestinationFunction(Integer i) {
        switch(i) {
            /**
             * The destination value of the blend function is all zeros.
             */
            case DB_ZERO:
                return BlendState.DestinationFunction.Zero;
            /**
             * The destination value of the blend function is all ones.
             */
            case DB_ONE:
                return BlendState.DestinationFunction.One;
            /**
             * The destination value of the blend function is the source color.
             */
            case DB_SRC_COLOR:
                return BlendState.DestinationFunction.SourceColor;
            /**
             * The destination value of the blend function is 1 - the source color.
             */
            case DB_ONE_MINUS_SRC_COLOR:
                return BlendState.DestinationFunction.OneMinusSourceColor;
            /**
             * The destination value of the blend function is the source alpha
             * value.
             */
            case DB_SRC_ALPHA:
                return BlendState.DestinationFunction.SourceAlpha;
            /**
             * The destination value of the blend function is 1 - the source alpha
             * value.
             */
            case DB_ONE_MINUS_SRC_ALPHA:
                return BlendState.DestinationFunction.OneMinusSourceAlpha;
            /**
             * The destination value of the blend function is the destination alpha
             * value.
             */
            case DB_DST_ALPHA:
                return BlendState.DestinationFunction.DestinationAlpha;
            /**
             * The destination value of the blend function is 1 - the destination
             * alpha value.
             */
            case DB_ONE_MINUS_DST_ALPHA:
                return BlendState.DestinationFunction.OneMinusDestinationAlpha;
        }
        return null;
    }

    // TEST FUNCTIONS
    public final static int TF_NEVER = 0;
    public final static int TF_LESS = 1;
    public final static int TF_EQUAL = 2;
    public final static int TF_LEQUAL = 3;
    public final static int TF_GREATER = 4;
    public final static int TF_NOTEQUAL = 5;
    public final static int TF_GEQUAL = 6;
    public final static int TF_ALWAYS = 7;

    static BlendState.TestFunction getBlendTestFunction(Integer i) {
        switch(i) {
            /**
             * Never passes the depth test.
             */
            case TF_NEVER:
                return BlendState.TestFunction.Never;
            /**
             * Pass the test if this alpha is less than the reference alpha.
             */
            case TF_LESS:
                return BlendState.TestFunction.LessThan;
            /**
             * Pass the test if this alpha is equal to the reference alpha.
             */
            case TF_EQUAL:
                return BlendState.TestFunction.EqualTo;
            /**
             * Pass the test if this alpha is less than or equal to the reference alpha.
             */
            case TF_LEQUAL:
                return BlendState.TestFunction.LessThanOrEqualTo;
            /**
             * Pass the test if this alpha is greater than the reference alpha.
             */
            case TF_GREATER:
                return BlendState.TestFunction.GreaterThan;
            /**
             * Pass the test if this alpha is not equal to the reference alpha.
             */
            case TF_NOTEQUAL:
                return BlendState.TestFunction.NotEqualTo;
            /**
             * Pass the test if this alpha is greater than or equal to the reference
             * alpha.
             */
            case TF_GEQUAL:
                return BlendState.TestFunction.GreaterThanOrEqualTo;
            /**
             * Always passes the depth test.
             */
            case TF_ALWAYS:
                return BlendState.TestFunction.Always;
        }
        return null;
    }


    public static final int MF_FRONT = 0;
    public static final int MF_BACK = 1;
    public static final int MF_FRONT_AND_BACK = 2;

    static MaterialState.MaterialFace getMaterialFace(Integer i) {
        switch(i) {
            case MF_FRONT:
                /** Apply materials to front face only. This is default. */
                return MaterialState.MaterialFace.Front;
            case MF_BACK:
                /** Apply materials to back face only. */
                return MaterialState.MaterialFace.Back;
            case MF_FRONT_AND_BACK:
                /** Apply materials to front and back faces. */
                return MaterialState.MaterialFace.FrontAndBack;
        }

        return null;
    }

    public static final int CM_NONE = 0;
    public static final int CM_AMBIENT = 1;
    public static final int CM_DIFFUSE = 2;
    public static final int CM_AMBIENT_AND_DIFFUSE = 3;
    public static final int CM_SPECULAR = 4;
    public static final int CM_EMISSIVE = 5;

    static MaterialState.ColorMaterial getColorMaterial(Integer i) {
        switch(i) {
            /** Geometry colors are ignored. This is default. */
            case CM_NONE:
                return MaterialState.ColorMaterial.None;
            /** Geometry colors determine material ambient color. */
            case CM_AMBIENT:
                return MaterialState.ColorMaterial.Ambient;
            /** Geometry colors determine material diffuse color. */
            case CM_DIFFUSE:
                return MaterialState.ColorMaterial.Diffuse;
            /** Geometry colors determine material ambient and diffuse colors. */
            case CM_AMBIENT_AND_DIFFUSE:
                return MaterialState.ColorMaterial.AmbientAndDiffuse;
            /** Geometry colors determine material specular colors. */
            case CM_SPECULAR:
                return MaterialState.ColorMaterial.Specular;
            /** Geometry colors determine material emissive color. */
            case CM_EMISSIVE:
                return MaterialState.ColorMaterial.Emissive;
        }

        return null;
    }


    public static final int WM_CLAMP_S_CLAMP_T = 0;
    public static final int WM_CLAMP_S_WRAP_T = 1;
    public static final int WM_WRAP_S_CLAMP_T = 2;
    public static final int WM_WRAP_S_WRAP_T = 3;
    public static final int WM_ECLAMP_S_ECLAMP_T = 4;
    public static final int WM_BCLAMP_S_BCLAMP_T = 5;
    public static final int WM_MIRRORED_S_MIRRORED_T = 6;


    static void setTextureWrap(Integer i, Texture t) {
        switch(i) {
            /**
             * Wrapping modifier that clamps both the S and T values of the texture.
             */
            case WM_CLAMP_S_CLAMP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Clamp);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Clamp);
                break;

            /**
             * Wrapping modifier that clamps the S value but wraps the T value of the
             * texture.
             */
            case WM_CLAMP_S_WRAP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Clamp);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Repeat);
                break;


            /**
             * Wrapping modifier that wraps the S value but clamps the T value of the
             * texture.
             */
            case WM_WRAP_S_CLAMP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Clamp);
                break;


            /**
             * Wrapping modifier that wraps both the S and T values of the texture.
             */
            case WM_WRAP_S_WRAP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.Repeat);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Repeat);
                break;


            /**
             * Wrapping modifier that clamps both the S and T values of the texture.  Uses CLAMP_TO_EDGE.
             */
            case WM_ECLAMP_S_ECLAMP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.EdgeClamp);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.EdgeClamp);
                break;


            /**
             * Wrapping modifier that clamps both the S and T values of the texture.  Uses CLAMP_TO_BORDER.
             */
            case WM_BCLAMP_S_BCLAMP_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.BorderClamp);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.BorderClamp);
                break;


            /**
             * Wrapping modifier that uses a texture twice the size of the original image with the second half being a
             * mirrored image of the first.  Uses MIRRORED_REPEAT.
             */
            case WM_MIRRORED_S_MIRRORED_T:
                t.setWrap(Texture.WrapAxis.S, Texture.WrapMode.MirroredRepeat);
                t.setWrap(Texture.WrapAxis.T, Texture.WrapMode.MirroredRepeat);
                break;
        }
    }

    public static final int WS_FRONT_AND_BACK = 0;
    public static final int WS_FRONT = 1;
    public static final int WS_BACK = 2;

    public static WireframeState.Face getWireFrameFace(Integer i) {
        switch(i) {
            /** Both sides of the model are wireframed. */
            case WS_FRONT_AND_BACK:
                return WireframeState.Face.FrontAndBack;
            /** The front will be wireframed, but the back will be solid. */
            case WS_FRONT:
                return WireframeState.Face.Front;
            /** The back will be wireframed, but the front will be solid. */
            case WS_BACK:
                return WireframeState.Face.Back;
        }
        return null;
    }

    static float[] getHeightMap(int[] src) {
        if (src == null) {
            return null;
        }

        float[] result = new float[src.length];
        for(int i = 0; i < src.length; i++) {
            result[i] = src[i];
        }

        return result;
    }

    public static Texture tmLoadTexture(URL file, int minFilter, int magFilter, int imageType, float anisoLevel, boolean flipped) {


        return TextureManager.loadTexture(file, getMinificationFilter(minFilter), getMagnificationFilter(magFilter), getFormat(imageType), anisoLevel, flipped);
    }


   public static final int GUESS_FORMAT_NO_S3TC = -2;
   public static final int GUESS_FORMAT = -1;
   public static final int RGBA4444 = 0;
   public static final int RGB888 = 1;
   public static final int RGBA5551 = 2;
   public static final int RGBA8888 = 3;
   public static final int RA88 = 4;
   public static final int RGB888_DXT1 = 5;
   public static final int RGBA8888_DXT1A = 6;
   public static final int RGBA8888_DXT3 = 7;
   public static final int RGBA8888_DXT5 = 8;
   public static final int LAST_UNCOMPRESSED_TYPE = RGBA8888_DXT5;
   public static final int DXT1_NATIVE = 9;
   public static final int DXT1A_NATIVE = 10;
   public static final int DXT3_NATIVE = 11;
   public static final int DXT5_NATIVE = 12;
   public static final int LAST_TYPE = DXT5_NATIVE;

    private static Image.Format getFormat(int i) {
        switch(i) {
            /**
                * When used in texture loading, this indicates to let jME guess
                * the format, but not to use S3TC compression, even if available.
                */
               case GUESS_FORMAT_NO_S3TC:
                   return Image.Format.GuessNoCompression;
               /**
                * When used in texture loading, this indicates to let jME guess
                * the format.  jME will use S3TC compression, if available.
                */
               case GUESS_FORMAT:
                   return Image.Format.Guess;

               /**
                * 16-bit RGBA with 4 bits for each component.
                */
               case RGBA4444:
                   return Image.Format.RGBA4;
               /**
                * 24-bit RGB with 8 bits for each component.
                */
               case RGB888:
                   return Image.Format.RGB8;
               /**
                * 16-bit RGBA with 5 bits for color components and 1 bit for alpha.
                */
               case RGBA5551:
                   return Image.Format.RGB5A1;
               /**
                * 32-bit RGBA with 8 bits for each component.
                */
               case RGBA8888:
                   return Image.Format.RGBA8;

               /**
                * 16-bit RA with 8 bits for red and 8 bits for alpha.
                */
               case RA88:
                   return Image.Format.Luminance8Alpha8;

               /**
                * RGB888, compressed to DXT-1 internally.
                */
               case RGB888_DXT1:
                   return Image.Format.RGB_TO_DXT1;


               /**
                * RGBA8888, compressed to DXT-1A internally.
                */
               case RGBA8888_DXT1A:
                   return Image.Format.RGBA_TO_DXT1;

               /**
                * RGBA8888, compressed to DXT-3 internally.
                */
               case RGBA8888_DXT3:
                   return Image.Format.RGBA_TO_DXT3;

               /**
                * RGBA8888, compressed to DXT-5 internally.
                */
               case RGBA8888_DXT5:
                   return Image.Format.RGBA_TO_DXT5;

               /**
                * DXT-1 compressed format, no alpha.
                */
               case DXT1_NATIVE:
                   return Image.Format.NativeDXT1;

               /**
                * DXT-1 compressed format, one bit alpha.
                */
               case DXT1A_NATIVE:
                   return Image.Format.NativeDXT1A;


               /**
                * DXT-3 compressed format, with alpha.
                */
               case DXT3_NATIVE:
                   return Image.Format.NativeDXT3;

               /**
                * DXT-5 compressed format, with alpha.
                */
            case DXT5_NATIVE:
                return Image.Format.NativeDXT5;

        }
        return null;
    }

    public static final int MM_NONE = 0;
    public static final int MM_NEAREST = 1;
    public static final int MM_LINEAR = 2;
    public static final int MM_NEAREST_NEAREST = 3;
    public static final int MM_NEAREST_LINEAR = 4;
    public static final int MM_LINEAR_NEAREST = 5;
    public static final int MM_LINEAR_LINEAR = 6;


    private static Texture.MinificationFilter getMinificationFilter(int i) {
        switch(i) {


                /**
                 * Mipmap option to return the value of the texture element that is
                 * nearest to the center of the pixel being textured.
                 */
            case MM_NEAREST:
                /*
                * Nearest neighbor interpolation is the fastest and crudest filtering
                * method - it simply uses the color of the texel closest to the pixel
                * center for the pixel color. While fast, this results in aliasing and
                * shimmering during minification. (GL equivalent: GL_NEAREST)
                */
                return Texture.MinificationFilter.NearestNeighborNoMipMaps;


            /**
             * Mipmap option that picks the mipmap that most closely matches the size
             * of the pixel being textured and uses MM_NEAREST criteria.
             */
            case MM_NEAREST_NEAREST:
                /**
                 * Same as NearestNeighborNoMipMaps except that instead of using samples
                 * from texture level 0, the closest mipmap level is chosen based on
                 * distance. This reduces the aliasing and shimmering significantly, but
                 * does not help with blockiness. (GL equivalent:
                 * GL_NEAREST_MIPMAP_NEAREST)
                 */
                return Texture.MinificationFilter.NearestNeighborNearestMipMap;


            /**
             * Mipmap option that picks the two mipmaps that most closely match the
             * size of the pixel being textured and uses MM_NEAREST criteria.
             */
            case MM_LINEAR_NEAREST:
                /**
                 * Similar to NearestNeighborNoMipMaps except that instead of using
                 * samples from texture level 0, a sample is chosen from each of the
                 * closest (by distance) two mipmap levels. A weighted average of these
                 * two samples is returned. (GL equivalent: GL_NEAREST_MIPMAP_LINEAR)
                 */
                return Texture.MinificationFilter.NearestNeighborLinearMipMap;


            /**
             * Mipmap option for no mipmap.
             */
            case MM_NONE:
            /**
             * Mipmap option to return the weighted average of the four texture
             * elements that are closest to the center of the pixel being textured.
             */
            case MM_LINEAR:
                /**
                 * In this method the four nearest texels to the pixel center are
                 * sampled (at texture level 0), and their colors are combined by
                 * weighted averages. Though smoother, without mipmaps it suffers the
                 * same aliasing and shimmering problems as nearest
                 * NearestNeighborNoMipMaps. (GL equivalent: GL_LINEAR)
                 */
                return Texture.MinificationFilter.BilinearNoMipMaps;

            /**
             * Mipmap option that picks the mipmap most closely matches the size of the
             * pixel being textured and uses MM_LINEAR criteria.
             */
            case MM_NEAREST_LINEAR:
                /**
                 * Same as BilinearNoMipMaps except that instead of using samples from
                 * texture level 0, the closest mipmap level is chosen based on
                 * distance. By using mipmapping we avoid the aliasing and shimmering
                 * problems of BilinearNoMipMaps. (GL equivalent:
                 * GL_LINEAR_MIPMAP_NEAREST)
                 */
                return Texture.MinificationFilter.BilinearNearestMipMap;


            /**
             * Mipmap option that picks the two mipmaps that most closely match the
             * size of the pixel being textured and uses MM_LINEAR criteria.
             */
            case MM_LINEAR_LINEAR:
                /**
                 * Trilinear filtering is a remedy to a common artifact seen in
                 * mipmapped bilinearly filtered images: an abrupt and very noticeable
                 * change in quality at boundaries where the renderer switches from one
                 * mipmap level to the next. Trilinear filtering solves this by doing a
                 * texture lookup and bilinear filtering on the two closest mipmap
                 * levels (one higher and one lower quality), and then linearly
                 * interpolating the results. This results in a smooth degradation of
                 * texture quality as distance from the viewer increases, rather than a
                 * series of sudden drops. Of course, closer than Level 0 there is only
                 * one mipmap level available, and the algorithm reverts to bilinear
                 * filtering (GL equivalent: GL_LINEAR_MIPMAP_LINEAR)
                 */
                return Texture.MinificationFilter.Trilinear;
        }
        return null;
    }

    public static final int FM_NEAREST = 0;
    public static final int FM_LINEAR = 1;

    static Texture.MagnificationFilter getMagnificationFilter(int i) {
        switch(i) {
            /**
             * Filter option to return the value of the texture element that is
             * nearest to the center of the pixel being textured.
             */
            case FM_NEAREST:
                return Texture.MagnificationFilter.NearestNeighbor;

            /**
             * Filter option to return the weighted average of the four texture
             * elements that are closest to the center of the pixel being textured.
             */
            case FM_LINEAR:
                return Texture.MagnificationFilter.Bilinear;

        }

        return null;

    }
}
