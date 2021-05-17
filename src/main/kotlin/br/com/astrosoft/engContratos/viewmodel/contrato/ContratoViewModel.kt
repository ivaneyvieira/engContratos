package br.com.astrosoft.engContratos.viewmodel.contrato

import br.com.astrosoft.framework.viewmodel.IView
import br.com.astrosoft.framework.viewmodel.ViewModel

class ContratoViewModel(view: IContratoView) : ViewModel<IContratoView>(view) {
  val tabFornecedorViewModel = TabFornecedorViewModel(this)
  override fun listTab() = listOf(view.tabFornecedor)
}

interface IContratoView : IView {
  val tabFornecedor: ITabFornecedor
}