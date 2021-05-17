package br.com.astrosoft.engContratos.view.contrato

import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorCliente
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaDataNota
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaFatura
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaLoja
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaNota
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaValor
import br.com.astrosoft.engContratos.viewmodel.contrato.ITabFornecedor
import br.com.astrosoft.engContratos.viewmodel.contrato.TabFornecedorViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.SubWindowForm
import br.com.astrosoft.framework.view.TabPanelGrid
import br.com.astrosoft.framework.view.addColumnButton
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.getColumnBy
import com.github.mvysny.karibudsl.v10.onLeftClick
import com.github.mvysny.karibudsl.v10.textField
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.value.ValueChangeMode.TIMEOUT
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TabFornededor(val viewModel: TabFornecedorViewModel) : TabPanelGrid<Fornecedor>(Fornecedor::class),
                                                             ITabFornecedor {
  private lateinit var edtLocalizador: TextField

  override fun filtro(): FiltroFonecedor {
    return FiltroFonecedor(edtLocalizador.value ?: "")
  }

  override fun selecionaEmail(nota: NotaEntrada, emails: List<EmailDB>) {
    TODO("Not yet implemented")
  }

  override fun editFile(nota: NotaEntrada, insert: (NFFile) -> Unit) {
    TODO("Not yet implemented")
  }

  override fun editRmk(nota: NotaEntrada, save: (NotaEntrada) -> Unit) {
    TODO("Not yet implemented")
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
      DlgNota(viewModel).showDialogNota(fornecedor)
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

val Fornecedor.labelTitle: String
  get() = "$vendno - $nomeFantasia"

class DlgNota(val viewModel: TabFornecedorViewModel) {
  fun showDialogNota(fornecedor: Fornecedor?) {
    fornecedor ?: return
    lateinit var gridNota: Grid<NotaEntrada>
    val listNotas = fornecedor.notaFornecedor()
    val form = SubWindowForm(fornecedor.labelTitle, toolBar = {
      val captionImpressoa = "Impressão"
      button(captionImpressoa) {
        icon = PRINT.create()
        onLeftClick {
          val notas = gridNota.asMultiSelect().selectedItems.toList()
          viewModel.imprimirNotaContrato(notas)
        }
      }
    }) {
      gridNota = createGridNotas(listNotas)
      gridNota
    }
    form.open()
  }

  private fun filename(): String {
    val sdf = DateTimeFormatter.ofPattern("yyMMddHHmmss")
    val textTime = LocalDateTime.now().format(sdf)
    return "notas$textTime.xlsx"
  }

  private fun createGridNotas(listNotas: List<NotaEntrada>): Grid<NotaEntrada> {
    val gridDetail = Grid(NotaEntrada::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setSelectionMode(MULTI)
      setItems(listNotas) //
      addColumnButton(FILE_PICTURE, "Arquivos", "Arq") { nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(EDIT, "Editor", "Edt") { nota ->
        viewModel.editRmk(nota)
      }
      addColumnButton(ENVELOPE_O, "Editor", "Email") { nota ->
        viewModel.mostrarEmailNota(nota)
      }

      notaLoja()
      notaDataNota()
      notaNota()
      notaFatura()
      notaValor().apply {
        val totalPedido = listNotas.sumOf { it.valor }.format()
        setFooter(Html("<b><font size=4>Total R$ &nbsp;&nbsp;&nbsp;&nbsp; ${totalPedido}</font></b>"))
      }
      sort(listOf(GridSortOrder(getColumnBy(NotaEntrada::dataNota), SortDirection.ASCENDING)))
    }
  }
}