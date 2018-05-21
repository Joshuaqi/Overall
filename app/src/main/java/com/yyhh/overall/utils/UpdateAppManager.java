package com.yyhh.overall.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.pgyersdk.update.UpdateManagerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yh on 2017/9/29.
 */

public class UpdateAppManager {

    // 外存sdcard存放路径
//    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "AutoUpdate" +"/";
    private   String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "AutoUpdate" +"/";

    //应用包名
    private String NAME="航科云警.apk";
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    // 下载应用存放全路径
    private String FILE_NAME = FILE_PATH +NAME;

    public void setFILE_PATH(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }


    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";

    private Context context;
    //获取版本数据的地址
    private String version_path = "http://47.92.151.73/versionnumber";
    //获取新版APK的默认地址
    private String apk_path = "http://47.92.151.73/sites/default/files/2017-09/航科云警app_205_7_ALI.apk";
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    public void setApk_path(String apk_path) {
        this.apk_path = apk_path;
    }

    //新版本号和描述语言
    private String update_versionCode;
    private String update_describe;

    public UpdateAppManager(Context context) {
        this.context = context;
    }

    /**
     * 获取当前版本号
     */
    private int getCurrentVersion() {
        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            AppLogger.d( "当前版本名和版本号" + info.versionName + "--" + info.versionCode);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            AppLogger.d("获取当前版本号出错");
            return 0;
        }
    }

    /**
     * 从服务器获得更新信息
     */
    public void getUpdateMsg() {

        class mAsyncTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {

                HttpURLConnection connection = null;
                try {
                    URL url_version = new URL(params[0]);
                    connection = (HttpURLConnection) url_version.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));

                    AppLogger.d("bufferReader读到的数据--" + reader);

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {             //回到主线程，更新UI

                AppLogger.d( "异步消息处理反馈--" + s);
                try {
                    JSONObject object = new JSONObject(s);

                    update_versionCode = object.optString("version");
                    apk_path="http://"+object.optString("file");
                    update_describe = "您有新的版本";
                    FILE_PATH=context.getExternalCacheDir()+"/" + "AutoUpdate" +"/";
                    FILE_NAME = FILE_PATH +NAME+update_versionCode+".apk";
                    AppLogger.d("新版本号--" + update_versionCode);
                    AppLogger.d("新版本描述--\n" + update_describe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (update_versionCode!=null){
                    int i = Integer.parseInt(update_versionCode);
                    if (i>getCurrentVersion()){
//                if (Integer.parseInt(update_versionCode.replace(".", "")) > Integer.parseInt(getCurrentVersion().replace(".", ""))) {
                        AppLogger.d( "提示更新！");
                        showNoticeDialog();
                    } else {
                        AppLogger.d("已是最新版本！");
                    }
                }else {
                    AppLogger.d("已是最新版本！");
                }

            }
        }

        new mAsyncTask().execute(version_path);
    }


    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(context)
                .setTitle("检测到新版本！")
                .setMessage(update_describe)
                .setCancelable(false)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new downloadAsyncTask().execute();
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            AppLogger.d("执行至--onPreExecute");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            AppLogger.e("执行至--doInBackground");

            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(apk_path);
                connection = (HttpURLConnection) url.openConnection();

                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;

                AppLogger.e("执行至--readLength = 0");

                while ((len = in.read(buffer)) != -1) {

                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;

                    int curProgress = (int) (((float) readLength / fileLength) * 100);

                    AppLogger.e("当前下载进度：" + curProgress);

                    publishProgress(curProgress);

                    if (readLength >= fileLength) {

                        AppLogger.e("执行至--readLength >= fileLength");
                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            AppLogger.e( "异步更新进度接收到的值：" + values[0]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            //已经存在
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                Uri apkUri = FileProvider.getUriForFile(context, "com.pzc.project.fileprovider", new File(appFile.toString()));  //包名.fileprovider
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }else {
            // 跳转到新版本应用安装页面
//        Utils.installapk(context,appFile.toString());
            UpdateManagerListener.updateLocalBuildNumber(result);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
                Uri apkUri = FileProvider.getUriForFile(context, "com.pzc.project.fileprovider", new File(appFile.toString()));  //包名.fileprovider
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        }


       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
    }
}
