node {
  def service = 'market-data'

  checkout scm
  
  stage('Build') {
    def tag = "${env.BRANCH_NAME}.${env.BUILD_NUMBER}"
    def image = docker.build("${service}:${tag}")
  }
}
