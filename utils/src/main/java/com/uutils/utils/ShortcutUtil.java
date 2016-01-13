package com.uutils.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;

public class ShortcutUtil {

    private static String ANDROID_LAUNCHER = "com.android.launcher";
    public static final String ACTION_ADD_SHORTCUT = ANDROID_LAUNCHER + ".action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = ANDROID_LAUNCHER + ".action.UNINSTALL_SHORTCUT";

    private static void addShortcut(Context context, Intent shortcutbroast) {
        String install = Build.VERSION.SDK_INT >= 19 ? Manifest.permission.INSTALL_SHORTCUT : "com.android.launcher.permission.INSTALL_SHORTCUT";
        if (PackageUtils.checkPermission(context, install)) {
            context.sendBroadcast(shortcutbroast);
        } else {
            Logs.w("dont't define " + install);
        }
    }

    public static void addShortcut(Context context, Intent shortcutIntent,
                                   String shortcutName, Bitmap icon, int iconId) {
        addShortcut(context, getInstallShortInfoIntent(context, shortcutIntent,
                shortcutName, icon, iconId));
    }

    private static void removeShortcut(Context context, Intent shortcutbroast) {
        String uninstall = Build.VERSION.SDK_INT >= 19 ? Manifest.permission.UNINSTALL_SHORTCUT : "com.android.launcher.permission.UNINSTALL_SHORTCUT";
        if (PackageUtils.checkPermission(context, uninstall)) {
            context.sendBroadcast(shortcutbroast);
        } else {
            Logs.w("dont't define " + uninstall);
        }
    }

    public static void removeShortcut(Context context, Intent shortcutIntent,
                                      String shortcutName, Bitmap icon, int iconId) {

        removeShortcut(context, getUnInstallShortInfoIntent(context, shortcutIntent,
                shortcutName, icon, iconId));
    }

    private static ShortcutIconResource getIconResource(Context context, int iconId)
            throws Resources.NotFoundException {
        if (iconId == 0) return null;
        return ShortcutIconResource.fromContext(context, iconId);
    }

    public static Intent getUnInstallShortInfoIntent(
            Context context, Intent shortcutIntent,
            String shortcutName, Bitmap icon, int iconId) {
        return getShortInfoIntent(false, context, shortcutIntent, shortcutName, icon,
                getIconResource(context, iconId));
    }

    public static Intent getInstallShortInfoIntent(
            Context context, Intent shortcutIntent,
            String shortcutName, Bitmap icon, int iconId) {
        return getShortInfoIntent(true, context, shortcutIntent, shortcutName, icon,
                getIconResource(context, iconId));
    }

    private static Intent getShortInfoIntent(boolean isAdd, Context context, Intent shortcutIntent,
                                             String shortcutName, Bitmap icon, ShortcutIconResource iconRes) {
        Intent shortcut = new Intent();
        if (isAdd) {
            shortcut.setAction(ACTION_ADD_SHORTCUT);
        } else {
            shortcut.setAction(ACTION_REMOVE_SHORTCUT);
        }
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
        shortcut.putExtra("duplicate", false); // 不允许重复创建
        //点击intent
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        // 图标1
        if (iconRes != null) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
            return shortcut;
        }
        // 快捷方式的图标2
        if (icon != null) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
            return shortcut;
        }
        return shortcut;
    }
}
