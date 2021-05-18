package br.com.astrosoft.engContratos.view.contrato.columns

import br.com.astrosoft.engContratos.model.beans.NFFile
import br.com.astrosoft.framework.view.addColumnLocalDate
import br.com.astrosoft.framework.view.addColumnString
import com.vaadin.flow.component.grid.Grid

object NFFileViewColumns {
  fun Grid<NFFile>.nfFileData() = addColumnLocalDate(NFFile::date) {
    this.setHeader("Data")
  }

  fun Grid<NFFile>.nfFileDescricao() = addColumnString(NFFile::nome) {
    this.setHeader("Descrição")
  }
}