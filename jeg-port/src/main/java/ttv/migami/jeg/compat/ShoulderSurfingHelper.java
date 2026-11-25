package ttv.migami.jeg.compat;

/**
 * Author: NineZero
 * <p>
 * PORT-DROPPED: Shoulder Surfing Reloaded compat. The original reflected into the mod (and imported its API class
 * com.github.exopandora.shouldersurfing.api.model.Perspective). The public methods are kept as inert stubs so the
 * client handlers that call them keep compiling and behave as vanilla (never shoulder surfing).
 */
@SuppressWarnings("unused")
public class ShoulderSurfingHelper
{
    public static boolean isShoulderSurfing()
    {
        return false;
    }

    public static void changePerspective(String perspective)
    {
    }
}
