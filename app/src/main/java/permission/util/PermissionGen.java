package permission.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import permission.util.internal.Utils;

/**
 * Created by zzx on 17-11-4.
 */

public class PermissionGen {
    private String[] mPermissions;
    private int mRequestCode;
    private Object object;
    private static Activity activity;
    private static ArrayList<String> shouldShowPermissions = new ArrayList<String>();
    private static ArrayList<String> againRequestPermissions = new ArrayList<>();

    private static final int REQUEST_AGAIN = 1;

    private static boolean isFromAgain = false;

    private PermissionGen(Object object) {
        this.object = object;
        if (object instanceof Fragment) {
            this.activity = ((Fragment) object).getActivity();
        } else if (object instanceof Activity) {
            this.activity = ((Activity) object);
        } else {
            throw new IllegalArgumentException("context must Fragment or Activity");
        }
    }

    public static PermissionGen with(Activity activity) {
        return new PermissionGen(activity);
    }

    public static PermissionGen with(Fragment fragment) {
        return new PermissionGen(fragment);
    }

    public PermissionGen permissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    public PermissionGen addRequestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    @TargetApi(value = Build.VERSION_CODES.M)
    public void request() {
        if (object == null) {
            throw new IllegalArgumentException("no context");
        }
        requestPermissions(object, mRequestCode, mPermissions);
    }

    public static void needPermission(Activity activity, int requestCode, String[] permissions) {
        requestPermissions(activity, requestCode, permissions);
    }

    public static void needPermission(Fragment fragment, int requestCode, String[] permissions) {
        requestPermissions(fragment, requestCode, permissions);
    }

    public static void needPermission(Activity activity, int requestCode, String permission) {
        requestPermissions(activity, requestCode, new String[] {permission});
    }

    public static void needPermission(Fragment fragment, int requestCode, String permission) {
        requestPermissions(fragment, requestCode, new String[] {permission});
    }
    @TargetApi(value = Build.VERSION_CODES.M)
    private static void requestPermissions(Object object, int requestCode, String[] permissions) {
        if (!Utils.isOverMarshmallow()) {
            doExecuteSuccess(object, requestCode);
            return ;
        }
        shouldShowPermissions.clear();
        List<String> deniedPermissions = Utils.
                findDeniedPermissions(Utils.getActivity(object), permissions);
        List<String> requestPermissions = new ArrayList<>();
        for (String permission : deniedPermissions) {
            if (object instanceof Fragment) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(((Fragment) object)
                    .getActivity(), permission)) {
                    shouldShowPermissions.add(permission);
                } else {
                    requestPermissions.add(permission);
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) object),
                        permission)) {
                    shouldShowPermissions.add(permission);
                } else {
                    requestPermissions.add(permission);
                }
            }
        }

        if (requestPermissions.size() > 0) {
            isFromAgain = false;
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(requestPermissions
                                .toArray(new String[requestPermissions.size()]),
                                requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(requestPermissions
                                .toArray(new String[requestPermissions.size()]),
                                requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + "is not supported");
            }
        } else if (shouldShowPermissions.size() > 0){
            isFromAgain = true;
            againRequestPermissions.clear();
            for (String permission : shouldShowPermissions) {
                showDialog(permission, activity, object, requestCode);
            }
        }
    }

    private static void showDialog(final String permission, Activity activity,
                                   final Object object, final int requestCode) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage("拒绝 " + permission + " 权限"
                    + " 将不能使用某些功能,设置为同意?")
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        againRequestPermissions.add(permission);
                        requestPermissionAgain(object, requestCode, permission);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissionAgain(Object object, int requestCode, String permission) {
        if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(new String[] {permission}, requestCode);
        } else {
            ((Activity) object).requestPermissions(new String[] {permission}, requestCode);
        }
    }

    private static void doExecuteSuccess(Object activity, int requestCode) {
        Method executeMethod = Utils.findMethodWithRequestCode(activity.getClass(),
                PermissionSuccess.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void doExecuteFail(Object activity, int requestCode) {
        Method executeMethod = Utils.findMethodWithRequestCode(activity.getClass(),
                PermissionFail.class, requestCode);
        executeMethod(activity, executeMethod);
    }

    private static void executeMethod(Object activity, Method executeMethod) {
        if (executeMethod != null) {
            try {
                if (!executeMethod.isAccessible()) {
                    executeMethod.setAccessible(true);
                }
                executeMethod.invoke(activity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(Activity activity, int requestCode,
                                                  String[] permissions, int[] grantResults) {
        requestResult(activity, requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(Fragment fragment, int requestCode,
                                                  String[] permissions, int[] grantResult) {
        requestResult(fragment, requestCode, permissions, grantResult);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestResult(Object obj, int requestCode, String[] permissions,
                                     int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<String>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }

        if (deniedPermissions.size() > 0) {
            doExecuteFail(obj, requestCode);
        } else {
            doExecuteSuccess(obj, requestCode);
        }
        if (!isFromAgain) {
            if (shouldShowPermissions.size() > 0) {
                for (String permission : shouldShowPermissions) {
                    againRequestPermissions.clear();
                    requestPermissionAgain(obj, requestCode, permission);
                }
            }
        }
    }
}
