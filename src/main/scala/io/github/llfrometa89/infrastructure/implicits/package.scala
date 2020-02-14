package io.github.llfrometa89.infrastructure

import io.github.llfrometa89.application.services.{AuthServiceInstances, UserServiceInstances}
import io.github.llfrometa89.infrastructure.configurations.ConfigFactoryInstances
import io.github.llfrometa89.infrastructure.gateways.UserCognitoGatewayInstances
import io.github.llfrometa89.infrastructure.repositories.UserProfileMysqlRepositoryInstances

package object implicits
    extends ConfigFactoryInstances
    with UserCognitoGatewayInstances
    with UserProfileMysqlRepositoryInstances
    with UserServiceInstances
    with AuthServiceInstances {
  object validated extends syntax.ValidatedSyntax
}
