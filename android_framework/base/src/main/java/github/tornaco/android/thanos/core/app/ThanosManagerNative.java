package github.tornaco.android.thanos.core.app;

import android.os.ServiceManager;

import github.tornaco.android.thanos.core.IThanos;
import github.tornaco.android.thanos.core.T;
import util.Singleton;

public class ThanosManagerNative {

    private static Singleton<IThanos> sIThanosSingleton = new Singleton<IThanos>() {
        @Override
        protected IThanos create() {
            return IThanos.Stub.asInterface(
                    ServiceManager.getService(T.serviceInstallName()));
        }
    };

    public static IThanos getDefault() {
        return sIThanosSingleton.get();
    }

    public static boolean isServiceInstalled() {
        return ServiceManager.getService(T.serviceInstallName()) != null;
    }
}
