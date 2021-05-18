package br.com.astrosoft.engContratos.view.contrato.columns

import br.com.astrosoft.engContratos.model.beans.Fornecedor
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object FornecedorViewColumns {
  fun Grid<Fornecedor>.fornecedorCodigo() = addColumnInt(Fornecedor::vendno) {
    this.setHeader("Fornecedor")
  }

  fun Grid<Fornecedor>.fornecedorNome() = addColumnString(Fornecedor::nomeFantasia) {
    this.setHeader("Fornecedor")
  }

  fun Grid<Fornecedor>.fornecedorUltimaData() = addColumnLocalDate(Fornecedor::ultimaData) {
    this.setHeader("Ultima Data")
  }
}