package easy.math

/**
 * Created by moran on 04/06/15.
 */
class GeneralUtils {

    public static String errorJson(String reason) {

        return String.format("{\"reason\":\"%s\"}", reason)
    }
}
