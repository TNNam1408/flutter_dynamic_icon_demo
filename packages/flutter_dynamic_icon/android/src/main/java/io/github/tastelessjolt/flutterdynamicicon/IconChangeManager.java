package io.github.tastelessjolt.flutterdynamicicon;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;
import static android.content.pm.PackageManager.GET_ACTIVITIES;
import static android.content.pm.PackageManager.GET_DISABLED_COMPONENTS;
import static android.content.pm.PackageManager.GET_META_DATA;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class IconChangeManager {

    private static final String META_DATA_KEY_ALIAS_TITLE = "com.example.dynamic_icon_method_flutter_0604.ALIAS_TITLE";

    private final Activity activity;
    private final PackageManager packageManager;
    private final List<Alias> iconAliases;
    private final Alias initialAlias;
    private final Alias cloneInitialAlias;
    private Alias currentAlias;
    private final boolean isIconChangeActivated;

    IconChangeManager(Activity activity) {
        this.activity = activity;
        this.packageManager = activity.getPackageManager();
        this.iconAliases = new ArrayList<>();

        Alias initial = null;
        Alias cloneInitial = null;
        Alias current = null;
        boolean isActivated;

        try {
            final PackageInfo packageInfo = getPackageInfo();
            for (ActivityInfo activityInfo : packageInfo.activities) {
                if (isIconChangeActivityAlias(activityInfo)) {
                    final Alias alias = new Alias(activityInfo.name, activityInfo.metaData.getString(META_DATA_KEY_ALIAS_TITLE), activityInfo.icon);

                    if (isAliasComponentEnabled(activityInfo)) {
                        if (current == null) {
                            current = alias;
                        } else {
                            throw new IllegalStateException("Multiple aliases currently enabled");
                        }
                    }

                    if (activityInfo.enabled) {
                        if (initial == null) {
                            initial = alias;
                        } else {
                            throw new IllegalStateException("Multiple initial aliases enabled in manifest");
                        }
                    } else {
                        iconAliases.add(alias);
                    }
                }
            }

            if (current == null) {
                throw new IllegalStateException("No alias currently enabled");
            }

            if (initial == null) {
                throw new IllegalStateException("No initial alias enabled in manifest");
            } else {
                isActivated = packageManager.getComponentEnabledSetting(new ComponentName(activity, initial.className)) == COMPONENT_ENABLED_STATE_DISABLED;
            }

            for (Alias alias : iconAliases) {
                if (!alias.equals(initial) && alias.iconResId == initial.iconResId) {
                    if (cloneInitial == null) {
                        cloneInitial = alias;
                    } else {
                        throw new IllegalStateException("Multiple clone initial aliases found");
                    }
                }
            }

            if (cloneInitial == null) {
                throw new IllegalStateException("No clone initial alias found");
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        initialAlias = initial;
        cloneInitialAlias = cloneInitial;
        currentAlias = current;
        isIconChangeActivated = isActivated;
    }

    List<Alias> getAliases() {
        return iconAliases;
    }

    boolean isIconChangeActivated() {
        return isIconChangeActivated;
    }

    void setCurrentAlias(Alias alias) {
        if (!isIconChangeActivated) {
            setAliasComponentEnabled(cloneInitialAlias, true);
            setAliasComponentEnabled(initialAlias, false);
            if (cloneInitialAlias != alias) {
                setAliasComponentEnabled(cloneInitialAlias, false);
                setAliasComponentEnabled(alias, true);
            }
            activity.finish();
        } else if (currentAlias != alias) {
            setAliasComponentEnabled(currentAlias, false);
            setAliasComponentEnabled(alias, true);
            currentAlias = alias;
        }
    }

    private PackageInfo getPackageInfo() throws PackageManager.NameNotFoundException {
        return packageManager.getPackageInfo(activity.getPackageName(), GET_ACTIVITIES | GET_DISABLED_COMPONENTS | GET_META_DATA);
    }

    private boolean isIconChangeActivityAlias(ActivityInfo activityInfo) {
        return activityInfo.targetActivity != null && activityInfo.metaData != null && activityInfo.metaData.containsKey(META_DATA_KEY_ALIAS_TITLE);
    }

    private boolean isAliasComponentEnabled(ActivityInfo aliasInfo) {
        final int state = packageManager.getComponentEnabledSetting(createComponentName(aliasInfo.name));
        if (state == COMPONENT_ENABLED_STATE_DEFAULT) {
            return aliasInfo.enabled;
        }
        return state == COMPONENT_ENABLED_STATE_ENABLED;
    }

    private void setAliasComponentEnabled(Alias alias, boolean enabled) {
        final int newState = enabled ? COMPONENT_ENABLED_STATE_ENABLED : alias.equals(initialAlias) ? COMPONENT_ENABLED_STATE_DISABLED : COMPONENT_ENABLED_STATE_DEFAULT;

        packageManager.setComponentEnabledSetting(createComponentName(alias.className), newState, DONT_KILL_APP);
    }

    private ComponentName createComponentName(String className) {
        return new ComponentName(activity.getPackageName(), className);
    }

    public void onAliasSelected(String title) {
        for (Alias alias : getAliases()) {
            if (alias.title.equals(title)) {
                setCurrentAlias(alias);
                break;
            }
        }
    }

    private final Map<String, String> aliasMap = new HashMap<String, String>() {{
        put("getDataFromNativeOriginal", "Original Icon");
        put("getDataFromNativeRed", "Really Red");
        put("getDataFromNativeGreen", "Greatly Green");
        put("getDataFromNativeBlue", "Bigly Blue");
    }};

    public void activateIconChange() {
        if (!isIconChangeActivated) {
            setAliasComponentEnabled(cloneInitialAlias, true);
            setAliasComponentEnabled(initialAlias, false);
            activity.finish();
        }
    }

    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
//        if (Objects.equals(call.method, "enableDynamicIcon")) {
//            activateIconChange();
//            boolean isActivated = isIconChangeActivated();
//            result.success(isActivated);
//        } else {
        String alias = aliasMap.get(call.method);
        if (alias != null) {
            onAliasSelected(alias);
            result.success(getDataFromNative());
        } else {
            result.notImplemented();
        }
//        }
    }

    public String getDataFromNative() {
        // Perform platform-specific operations to fetch the data
        return "IconChangeSuccess";
    }
}
