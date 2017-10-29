node {
  def tag = "${env.BRANCH_NAME}.${env.BUILD_NUMBER}"
  def service = "market-data:${tag}"

  checkout scm

  stage('Build') {
    sh("docker build -t ${service} .")
  }

  stage('Test') {
    try {
      sh("""docker run \
          -v `pwd`:/workspace
          -w workspace --rm ${service} \
          py.test --junitxml results.xml""")
    } finally {
      step([$class: 'JUnitResultArchiver', testResults: 'results.xml'])
    }
  }

  def tagToDeploy = "morganjbruce/${service}"

  stage('Publish') {
    withDockerRegistry(registry: [credentialsId: 'dockerhub']) {
      sh("docker tag ${service} ${tagToDeploy}")
      sh("docker push ${tagToDeploy}")
    }
  }

  def deploy = load('deploy.groovy')

  stage('Deploy to staging') {
    deploy.toKubernetes(tagToDeploy, 'staging', 'market-data')
  }

  stage('Approve release?') {
    input message: "Release ${tagToDeploy} to production?"
  }

  stage('Deploy to production') {
    deploy.toKubernetes(tagToDeploy, 'production', 'market-data')
  }

  stage('Deploy canary') {
    deploy.toKubernetes(tagToDeploy, 'canary', 'market-data-canary')

    try {
      input message: "Continue releasing ${tagToDeploy} to production?"
    } catch (Exception e) {
      deploy.rollback('market-data-canary')
    }
  }
}
