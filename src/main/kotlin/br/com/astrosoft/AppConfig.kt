package br.com.astrosoft

import br.com.astrosoft.engContratos.model.saci
import br.com.astrosoft.engContratos.view.contrato.ContratoView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.framework.view.ViewUtil

object AppConfig {
  val mainClass = ContratoView::class
  val version = ViewUtil.versao
  const val appName = "contrato"
  const val commpany = "Engecopi"
  const val title = "Controle de Contratos"
  const val shortName = "Contratos"
  const val iconPath = "icons/logo.png"
  val user get() = SecurityUtils.userDetails
  val isAdmin get() = user?.admin == true
  fun findUser(username: String?): IUser? = saci.findUser(username)
}
