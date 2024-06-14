# [CLI Utility] Google Play App Version Code

Retrieve the latest (or next) version code for a Google Play App. Useful for automating build scripts.

## Setup

### Service Account

The CLI uses a Google Service Account `JSON` file to authenticate. 

Setup your service account according to the Expo documentation: https://github.com/expo/fyi/blob/main/creating-google-service-account.md

Ensure to download a `JSON` version of the key-file.

### Install the CLI

#### Latest Version

```shell
# Download the Latest Release
curl -LO https://github.com/inspire-labs-tms-tech/google-play-app-version-code-cli/releases/latest/download/google-app-version.deb

# Install the Latest Release
sudo apt-get install ./google-app-version.deb

# (Optional) Add the Latest Release to Path (to simply call `google-app-version` from any working directory)
sudo ln -s /usr/local/bin/google-app-version/bin/google-app-version /usr/bin
```

#### Specific Version

```shell
# Download a Specific Release
# Replace `v20240613.234416` in the URL with the desired/pinned version
curl -LO https://github.com/inspire-labs-tms-tech/google-play-app-version-code-cli/releases/download/v20240613.234416/google-app-version.deb

# Install the Release
sudo apt-get install ./google-app-version.deb

# (Optional) Add the Release to Path (to simply call `google-app-version` from any working directory)
sudo ln -s /usr/local/bin/google-app-version/bin/google-app-version /usr/bin
```
