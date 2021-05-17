package br.com.astrosoft.engContratos.view.contrato.columns

import br.com.astrosoft.engContratos.model.beans.NotaEntrada
import br.com.astrosoft.framework.view.addColumnDouble
import br.com.astrosoft.framework.view.addColumnInt
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NotaEntradaViewColumns {
  fun Grid<NotaEntrada>.notaLoja() = addColumnInt(NotaEntrada::loja) {
    this.setHeader("Loja")
  }

  fun Grid<NotaEntrada>.notaNi() = addColumnInt(NotaEntrada::ni) {
    this.setHeader("NI")
  }

  fun Grid<NotaEntrada>.notaNota() = addColumnString(NotaEntrada::nota) {
    this.setHeader("Nota")
  }

  fun Grid<NotaEntrada>.notaFatura() = addColumnString(NotaEntrada::fatura) {
    this.setHeader("Fatura")
  }

  fun Grid<NotaEntrada>.notaDataNota() = addColumnLocalDate(NotaEntrada::dataNota) {
    this.setHeader("Data")
  }

  fun Grid<NotaEntrada>.notaValor() = addColumnDouble(NotaEntrada::valor) {
    this.setHeader("Valor")
  }
}
