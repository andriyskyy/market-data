node {
  def service = 'market-data'
  def tag = "${env.BRANCH_NAME}.${env.BUILD_NUMBER}"

  checkout scm

  stage('Build') {
    sh("docker build -t ${service}:${tag} .")
  }
}
