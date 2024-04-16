Jenkins config for debian ubuntu linux using apt-get

# Install JDK
default is OpenJDK 17 at the time of this writing
```sudo apt-get install default-jdk```

Install Jenkins
```
sudo wget -O /usr/share/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install jenkins
```

grab first time password for later:
```
sudo nano /var/lib/jenkins/secrets/initialAdminPassword
```
If the server restarts do we need to change the given URL from first time setup?
```http://35.238.76.166:8080/```
After install, still using same address, jenkins is nice and fast.

Install docker
https://docs.docker.com/engine/install/debian/

get the key for apt
```
# Add Docker's official GPG key:
sudo apt-get update
sudo apt-get install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
```

install
```
 sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

then post setup to get docker to run without sudo
https://docs.docker.com/engine/install/linux-postinstall/
```
sudo groupadd docker
sudo usermod -aG docker $USER
```
That was for the current user account. We want to make sure the jenkins account can run docker commands without sudo:
```
sudo usermod -aG docker jenkins
```


restart may be required here?
change over to new group (or restart)
```
newgrp docker
```

and verify:
```
docker run hello-world
```


Install Maven v3.9.6:
Go to https://maven.apache.org/download.cgi and get the link for a different version, change URL and filenames below
```
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
tar -xvf apache-maven-3.9.6-bin.tar.gz
mv apache-maven-3.9.6 /opt/
```


Install Node & NPM via NVM:
```
# installs NVM (Node Version Manager)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash

# reload bashrc
. ~/.bashrc

# download and install Node.js
nvm install 20

# verifies the right Node.js version is in the environment
node -v # should print `v20.12.2`

# verifies the right NPM version is in the environment
npm -v # should print `10.5.0`
```

might need to add this to the jenkins bashrc?
```
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"  # This loads nvm
[ -s "$NVM_DIR/bash_completion" ] && \. "$NVM_DIR/bash_completion"  # This loads nvm ba>
```

