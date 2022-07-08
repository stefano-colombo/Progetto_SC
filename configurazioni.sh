#!/bin/bash

#versioni di docker e kubernetes
echo "[TASK 1] versioni di docker e kubernetes"
export KVERS=1.19.9-00
export DVERS='5:19.03.14~3-0~ubuntu-focal'

#Modificare /etc/hosts file mettendo i nomi corretti
echo "[TASK 2] modifiche file host"
sudo cat >> /etc/hosts << EOF
10.1.1.10 kmaster.example.com kmaster
10.1.1.4 kworker1.example.com mine1
10.1.1.11 kworker1.example.com kworker
EOF

#installare docker
echo "[TASK 3] installare docker"
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common -y
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update -y
sudo apt-get install docker-ce="$DVERS" docker-ce-cli="$DVERS" -y

#aggiungere l'utente ai gruppi sudo e docker
echo "[TASK 4] aggiungere l'utente ai gruppi"
sudo usermod -aG docker $USER
sudo usermod -aG sudo $USER

#abilitare il servizio docker
echo "[TASK 4] abilitare il servizio docker"
sudo systemctl enable docker >/dev/null 2>&1
sudo systemctl start docker

# aggiungere settings sysctl 
echo "[TASK 5] aggiungere settings sysctl"
cat >>/etc/sysctl.d/kubernetes.conf<<EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF
sysctl --system >/dev/null 2>&1

# disattivare SWAP"
echo "[TASK 6] disattivare SWAP"
sudo sed -i '/swap/d' /etc/fstab
sudo swapoff -a

# installare apt-transport-https pkg
echo "[TASK 7] installare apt-transport-https pkg"
sudo apt-get update && sudo apt-get install -y apt-transport-https 
sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -

# Add he kubernetes sources list into the sources.list directory 
echo "[TASK 8] Add he kubernetes sources list"
sudo cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF

ls -ltr /etc/apt/sources.list.d/kubernetes.list
sudo apt-get update -y

# Install Kubernetes kubeadm, kubelet and kubectl"
echo "[TASK 9] Add he kubernetes sources list"
sudo apt-get install -y kubelet=$KVERS kubeadm=$KVERS kubectl=$KVERS

# Enable and start kubelet service"
echo "[TASK 10] Enable and start kubelet service"
sudo systemctl enable kubelet >/dev/null 2>&1
sudo systemctl start kubelet >/dev/null 2>&1

# aggiungere settings al file sysctl.conf 
echo "[TASK 11] aggiungere settings al file sysctl.conf"
cat >>/etc/sysctl.conf<<EOF
net.ipv4.conf.default.rp_filter=1
net.ipv4.conf.all.rp_filter=1
net.ipv4.ip_forward=1
EOF
sudo sysctl -p
