package br.com.astrosoft.engContratos.view.contrato

import br.com.astrosoft.engContratos.view.ApplicaitonLayout
import br.com.astrosoft.engContratos.viewmodel.contrato.ContratoViewModel
import br.com.astrosoft.engContratos.viewmodel.contrato.IContratoView
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.ViewLayout
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("", layout = ApplicaitonLayout::class)
@PageTitle("Contratos")
@CssImport("./styles/gridTotal.css")
class ContratoView : ViewLayout<ContratoViewModel>(), IContratoView {
  override val viewModel = ContratoViewModel(this)
  override val tabFornecedor = TabFornededor(viewModel.tabFornecedorViewModel)

  override fun isAccept(user: IUser): Boolean {
    return true
  }

  init {
    addTabSheat(viewModel)
  }
}