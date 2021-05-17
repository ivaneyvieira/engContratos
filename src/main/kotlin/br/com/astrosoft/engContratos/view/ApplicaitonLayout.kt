package br.com.astrosoft.engContratos.view

import br.com.astrosoft.AppConfig
import br.com.astrosoft.engContratos.view.contrato.ContratoView
import br.com.astrosoft.framework.spring.LoginView
import br.com.astrosoft.framework.spring.SecurityUtils
import br.com.astrosoft.framework.spring.Session
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.drawer
import com.github.mvysny.karibudsl.v10.drawerToggle
import com.github.mvysny.karibudsl.v10.h3
import com.github.mvysny.karibudsl.v10.horizontalLayout
import com.github.mvysny.karibudsl.v10.hr
import com.github.mvysny.karibudsl.v10.icon
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.label
import com.github.mvysny.karibudsl.v10.navbar
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.routerLink
import com.github.mvysny.karibudsl.v10.tab
import com.github.mvysny.karibudsl.v10.tabs
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.dependency.JsModule
import com.vaadin.flow.component.icon.VaadinIcon.USER
import com.vaadin.flow.component.icon.VaadinIcon.WORKPLACE
import com.vaadin.flow.component.page.Push
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

@Theme(value = Lumo::class, variant = Lumo.DARK)
@Push
@PWA(name = AppConfig.title,
     shortName = AppConfig.shortName,
     iconPath = AppConfig.iconPath,
     enableInstallPrompt = false)
@JsModule("./styles/shared-styles.js")
class ApplicaitonLayout : AppLayout(), RouterLayout, BeforeEnterObserver {
  init {
    isDrawerOpened = true
    navbar {
      drawerToggle()
      h3(AppConfig.title)
      horizontalLayout {
        isExpand = true
      } //anchor("logout", "Sair")
      button("Sair") {
        onLeftClick {
          Session.current.close()
          ui.ifPresent {
            it.session.close()
            it.navigate("")
          }
        }
      }
    }
    drawer {
      verticalLayout {
        label("Versão ${AppConfig.version}")
        label(AppConfig.user?.login)
      }
      hr()
      tabs {
        orientation = Tabs.Orientation.VERTICAL

        tab {
          this.icon(WORKPLACE)
          routerLink(text = "Contrato", viewType = ContratoView::class)
        }
      }


      tabs {
        orientation = Tabs.Orientation.VERTICAL

        tab {
          this.isEnabled = AppConfig.isAdmin
          this.icon(USER)
          routerLink(text = "Usuário", viewType = UsuarioView::class)
        }
      }
    }
  }

  override fun beforeEnter(event: BeforeEnterEvent) {
    if (!SecurityUtils.isUserLoggedIn) {
      event.rerouteTo(LoginView::class.java)
    }
  }
}