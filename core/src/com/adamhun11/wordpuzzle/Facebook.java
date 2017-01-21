package com.adamhun11.wordpuzzle;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.Array;

import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;
import de.tomgrill.gdxfacebook.core.JsonResult;
import de.tomgrill.gdxfacebook.core.SignInMode;
import de.tomgrill.gdxfacebook.core.SignInResult;

/**
 * Created by Adam on 2017. 01. 14..
 */

public class Facebook {
    GDXFacebookConfig config;
    GDXFacebook facebook;

    public Facebook(){}
    public void init(){
        config = new GDXFacebookConfig();
        config.APP_ID = "224618371280693"; // required
        config.PREF_FILENAME = ".facebookSessionData"; // optional
        config.GRAPH_API_VERSION = "v2.6"; // optional, default is v2.6
    }
    public void logIn(){
        facebook = GDXFacebookSystem.install(config);
        Array<String> permissions = new Array<String>();
        permissions.add("email");
        permissions.add("public_profile");
        permissions.add("user_friends");

        facebook.signIn(SignInMode.READ, permissions, new GDXFacebookCallback<SignInResult>() {
            @Override
            public void onSuccess(SignInResult result) {
                // Login successful
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error handling
            }

            @Override
            public void onCancel() {
                // When the user cancels the login process
            }

            @Override
            public void onFail(Throwable t) {
                // When the login fails
            }
        });
    }

    public void share(){
        GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
        request.setMethod(Net.HttpMethods.POST);
        request.putField("message", "Hey use this libGDX extensions");
        //request.putField("link", "https://github.com/TomGrill/gdx-facebook");
        //request.putField("caption", "gdx-facebook");

        facebook.newGraphRequest(request, new GDXFacebookCallback<JsonResult>() {

            @Override
            public void onSuccess(JsonResult result) {
                // Success
            }

            @Override
            public void onError(GDXFacebookError error) {
                // Error
                System.out.println("Error");
            }

            @Override
            public void onFail(Throwable t) {
                // Fail
                System.out.println("Fail");
            }

            @Override
            public void onCancel() {
                // Cancel
            }

        });
    }

}
