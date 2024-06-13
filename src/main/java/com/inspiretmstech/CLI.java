package com.inspiretmstech;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.AppEdit;
import com.google.api.services.androidpublisher.model.Bundle;
import com.google.api.services.androidpublisher.model.BundlesListResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import picocli.CommandLine;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "version",
        version = "v00000000.000000",
        mixinStandardHelpOptions = true,
        description = "Obtains the latest version number from the Google Play Store"
)
public class CLI implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "the version number to output (${COMPLETION-CANDIDATES})")
    private VersionType versionType;
    @CommandLine.Option(names = {"-f", "--file"}, required = true, description = "a JSON file containing the service acount's credentials")
    private String file;
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "print with verbose output")
    private boolean verbose;
    @CommandLine.Option(names = {"-p", "--package"}, description = "the package name of the app")
    private String packageName;

    private void log(String msg) {
        if (this.verbose) System.out.println(msg);
    }

    private int getLatestVersion() throws Exception {
        assert Objects.nonNull(this.file);
        assert Objects.nonNull(this.packageName);

        this.log("Package: " + this.packageName);
        JsonObject json = JsonParser.parseString(this.file).getAsJsonObject();

        final String clientID = json.get("client_id").getAsString();
        final String clientEmail = json.get("client_email").getAsString();
        final String clientSecret = json.get("private_key").getAsString();
        final String clientSecretID = json.get("private_key_id").getAsString();

        this.log("Client ID: " + clientID);
        this.log("Client Email: " + clientEmail);
        this.log("Private Key ID: " + clientSecretID);

        final ServiceAccountCredentials credentials = ServiceAccountCredentials.fromPkcs8(clientID, clientEmail, clientSecret, clientSecretID, Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

        final AndroidPublisher publisher = new AndroidPublisher.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Inspire TMS Mobile").build();

        final AndroidPublisher.Edits edits = publisher.edits();
        AndroidPublisher.Edits.Insert editRequest = edits.insert(this.packageName, null);
        AppEdit appEdit = editRequest.execute();

        BundlesListResponse r = edits.bundles().list(this.packageName, appEdit.getId()).execute();
        int version = 0;
        this.log("Existing Bundles: " + r.getBundles().size());
        for (Bundle bundle : r.getBundles()) {
            int bundleVersion = bundle.getVersionCode();
            if (bundleVersion > version) version = bundleVersion;
            this.log("Found Bundle Version: " + bundleVersion);
        }

        if (version == 0) {
            this.log("version number is zero");
            System.out.println("Unable to determine version number");
            return 1;
        }

        return version;
    }

    @Override
    public Integer call() throws Exception {

        int currentVersion = this.getLatestVersion();

        switch (this.versionType) {
            case LATEST:
                if (this.verbose) this.log("Latest Bundle Version: " + currentVersion);
                else System.out.println(currentVersion);
                return 0;
            case NEXT:
                int nextBundleVersion = currentVersion + 1;
                if (this.verbose) this.log("Next Bundle Version: " + nextBundleVersion);
                else System.out.println(nextBundleVersion);
                return 0;
            default:
                throw new Exception("Invalid version type '" + this.versionType + "'");
        }
    }

    enum VersionType {
        LATEST,
        NEXT
    }

}
