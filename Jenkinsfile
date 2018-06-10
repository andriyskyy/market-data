def withPod(body) {
  podTemplate(label: 'pod', containers: [
      containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true)
    ],
    volumes: [
      hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock'),
    ]
 ) { body() }
}

withPod {
  node('pod') {
    def tag = "${env.BRANCH_NAME}.${env.BUILD_NUMBER}"
    def service = "market-data:${tag}"
    
    checkout scm

    stage('Build') {
      container('docker') {
        sh("docker build -t ${service} .")
      }
    }
  }
}
