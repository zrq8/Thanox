package github.tornaco.android.thanos.services.xposed.hooks.accessibility;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.android.thanos.core.pm.PackageManager;
import github.tornaco.android.thanos.core.util.Timber;
import github.tornaco.android.thanos.services.BootStrap;
import github.tornaco.android.thanos.services.accessibility.AccessibilityManagerServiceProxy;
import github.tornaco.android.thanos.services.apihint.Beta;
import github.tornaco.android.thanos.services.xposed.IXposedHook;
import github.tornaco.xposed.annotation.XposedHook;

import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._23;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._24;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._25;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._26;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._27;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._28;
import static github.tornaco.xposed.annotation.XposedHook.SdkVersions._29;

@XposedHook(targetSdkVersion = {_23, _24, _25, _26, _27, _28, _29})
@Beta
public class AccessibilityManagerServiceRgistry implements IXposedHook {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (PackageManager.packageNameOfAndroid().equals(lpparam.packageName)) {
            hookAccessibilityManagerServiceInit(lpparam);
        }
    }

    private void hookAccessibilityManagerServiceInit(XC_LoadPackage.LoadPackageParam lpparam) {
        Timber.d("hookAccessibilityManagerServiceInit...");
        try {
            // com/android/server/accessibility/AbstractAccessibilityServiceConnection.java
            Class clz = XposedHelpers.findClass("com.android.server.accessibility.AccessibilityManagerService",
                    lpparam.classLoader);
            Set unHooks = XposedBridge.hookAllConstructors(clz, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object service = param.thisObject;
                    BootStrap.THANOS_X.getWindowManagerService()
                            .onAccessibilityServiceAttach(new AccessibilityManagerServiceProxy(service));
                }
            });
            Timber.d("hookAccessibilityManagerServiceInit OK: " + unHooks);
        } catch (Exception e) {
            Timber.e(e, "Fail hookAccessibilityManagerServiceInit");
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) {
        // Noop.
    }
}
