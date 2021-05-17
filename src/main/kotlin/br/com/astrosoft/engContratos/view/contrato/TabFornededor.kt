package br.com.astrosoft.engContratos.view.contrato

import br.com.astrosoft.engContratos.model.beans.FiltroFonecedor
import br.com.astrosoft.engContratos.model.beans.Fornecedor
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.engContratos.viewmodel.contrato.ITabFornecedor
import br.com.astrosoft.engContratos.viewmodel.contrato.TabFornecedorViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT

class TabFornededor(val viewModel: TabFornecedorViewModel) : TabPanelGrid<Fornecedor>(Fornecedor::class),
                                                             ITabFornecedor {
  private lateinit var edtLocalizador: TextField

  override fun filtro(): FiltroFonecedor {
    return FiltroFonecedor(edtLocalizador.value ?: "")
  }


  override fun isAuthorized(user: IUser): Boolean {
    return true
  }

  override val label: String
    get() = "Fornecedores"

  override fun updateComponent() {
    viewModel.updateView()
  }

  override fun HorizontalLayout.toolBarConfig() {
    edtLocalizador = textField("Filtro") {
      width = "400px"
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        viewModel.updateView()
      }
    }
  }

  override fun Grid<Fornecedor>.gridPanel() {
    addColumnButton(FILE_TABLE, "Notas", "Notas") { fornecedor ->

    }
    addColumnButton(EDIT, "Editor", "Edt") { fornecedor ->

    }
    addColumnButton(PHONE_LANDLINE, "Representantes", "Rep") { fornecedor ->

    }
    fornecedorUltimaData()
    fornecedorCodigo()
    fornecedorCliente()
    fornecedorNome()
  }
}
