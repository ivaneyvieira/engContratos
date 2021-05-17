package br.com.astrosoft.engContratos.viewmodel.contrato

import br.com.astrosoft.engContratos.model.beans.FiltroFonecedor
import br.com.astrosoft.engContratos.model.beans.Fornecedor
import br.com.astrosoft.framework.viewmodel.ITabView

class TabFornecedorViewModel(val viewModel: ContratoViewModel)  {
  val subView
    get() = viewModel.view.tabFornecedor

  fun updateView() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }

  fun listFornecedores() : List<Fornecedor> {
    val filtro = subView.filtro()
    return Fornecedor.findFornecedores(filtro)
  }
}

interface ITabFornecedor : ITabView {
  fun filtro(): FiltroFonecedor
  fun updateGrid(itens: List<Fornecedor>)
}