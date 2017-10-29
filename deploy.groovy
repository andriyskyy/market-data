def toKubernetes(tagToDeploy, namespace, deploymentName) {
  sh("sed -i.bak 's#BUILD_TAG#${tagToDeploy}#' ./deploy/${namespace}/*.yml")
  kubectl("apply -f deploy/${namespace}/")

  waitOrRollback(namespace, deploymentName)
}

def kubectl(namespace, command) {
  sh("docker run --rm wernight/kubectl --namespace=${namespace} ${command}")
}

def waitOrRollback(namespace, deploymentName) {
  try {
    kubectl("rollout status --request-timeout='5m' deployment/${deploymentName}")
  } catch (Exception e) {
    rollback(deploymentName)
    throw e
  }
}

def rollback(deploymentName) {
  kubectl("rollout undo deployment/${deploymentName}")
}

return this;
