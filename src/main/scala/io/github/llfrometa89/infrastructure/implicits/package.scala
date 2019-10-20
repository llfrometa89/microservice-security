package io.github.llfrometa89.infrastructure

import io.github.llfrometa89.application.dto.{LoginDtoInstances, RegisterDtoInstances}
import io.github.llfrometa89.application.services.UserServiceInstances
import io.github.llfrometa89.infrastructure.configurations.PureConfigFactoryInstances
import io.github.llfrometa89.infrastructure.gateways.UserCognitoGatewayInstances
import io.github.llfrometa89.infrastructure.repositories.UserProfileMysqlRepositoryInstances

package object implicits
    extends LoginDtoInstances
    with RegisterDtoInstances
    with PureConfigFactoryInstances
    with UserCognitoGatewayInstances
    with UserProfileMysqlRepositoryInstances
    with UserServiceInstances
