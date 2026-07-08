package hexlet.code.util;

import hexlet.code.dto.BasePage;
import hexlet.code.types.FlashType;
import io.javalin.http.Context;

public final class Flash {

    private Flash() {
    }

    public static void set(Context ctx, String message, FlashType type) {
        ctx.sessionAttribute("flash", message);
        ctx.sessionAttribute("flashType", type);
    }

    public static void bind(Context ctx, BasePage page) {
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flashType"));
    }

    public static void info(Context ctx, String message) {
        set(ctx, message, FlashType.INFO);
    }

    public static void success(Context ctx, String message) {
        set(ctx, message, FlashType.SUCCESS);
    }

    public static void warning(Context ctx, String message) {
        set(ctx, message, FlashType.WARNING);
    }

    public static void danger(Context ctx, String message) {
        set(ctx, message, FlashType.DANGER);
    }
}
