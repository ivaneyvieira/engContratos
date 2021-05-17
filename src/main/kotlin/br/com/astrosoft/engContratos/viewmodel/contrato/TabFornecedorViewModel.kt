package br.com.astrosoft.engContratos.viewmodel.contrato

import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.fail

class TabFornecedorViewModel(val viewModel: ContratoViewModel) {
  val subView
    get() = viewModel.view.tabFornecedor

  fun updateView() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }

  fun listFornecedores(): List<Fornecedor> {
    val filtro = subView.filtro()
    return Fornecedor.findFornecedores(filtro)
  }

  fun imprimirNotaContrato(notas: List<NotaEntrada>) {
    TODO("Not yet implemented")
  }

  fun editFile(nota: NotaEntrada) = viewModel.exec {
    subView.editFile(nota) { nfFile ->
      nfFile.saveFile()
    }
  }

  fun editRmk(nota: NotaEntrada) = viewModel.exec {
    subView.editRmk(nota) { notaSaida ->
      notaSaida.saveRmk()
    }
  }

  fun mostrarEmailNota(nota: NotaEntrada?) = viewModel.exec {
    nota ?: fail("Não há nenhuma nota selecionada")
    val emails = nota.listEmailNota()
    subView.selecionaEmail(nota, emails)
  }
}

interface ITabFornecedor : ITabView {
  fun filtro(): FiltroFonecedor
  fun updateGrid(itens: List<Fornecedor>)
  fun selecionaEmail(nota: NotaEntrada, emails: List<EmailDB>)
  fun editFile(nota: NotaEntrada, insert: (NFFile) -> Unit)
  fun editRmk(nota: NotaEntrada, save: (NotaEntrada) -> Unit)
}