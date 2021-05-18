package br.com.astrosoft.engContratos.view.contrato

import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.engContratos.view.contrato.columns.EmailDBViewColumns.emailAssunto
import br.com.astrosoft.engContratos.view.contrato.columns.EmailDBViewColumns.emailData
import br.com.astrosoft.engContratos.view.contrato.columns.EmailDBViewColumns.emailEmail
import br.com.astrosoft.engContratos.view.contrato.columns.EmailDBViewColumns.emailHora
import br.com.astrosoft.engContratos.view.contrato.columns.EmailDBViewColumns.emailTipo
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorCodigo
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorNome
import br.com.astrosoft.engContratos.view.contrato.columns.FornecedorViewColumns.fornecedorUltimaData
import br.com.astrosoft.engContratos.view.contrato.columns.NFFileViewColumns.nfFileData
import br.com.astrosoft.engContratos.view.contrato.columns.NFFileViewColumns.nfFileDescricao
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaDataNota
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaFatura
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaLoja
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaNota
import br.com.astrosoft.engContratos.view.contrato.columns.NotaEntradaViewColumns.notaValor
import br.com.astrosoft.engContratos.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.engContratos.viewmodel.contrato.IEmailView
import br.com.astrosoft.engContratos.viewmodel.contrato.ITabFornecedor
import br.com.astrosoft.engContratos.viewmodel.contrato.TabFornecedorViewModel
import br.com.astrosoft.framework.model.IUser
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.util.htmlToText
import br.com.astrosoft.framework.view.*
import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.Html
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.Grid.SelectionMode.MULTI
import com.vaadin.flow.component.grid.GridSortOrder
import com.vaadin.flow.component.grid.GridVariant.LUMO_COMPACT
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon.*
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.FileRejectedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
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
    DlgSelecionaEmail(viewModel).selecionaEmail(nota, emails)
  }

  override fun editFile(nota: NotaEntrada, insert: (NFFile) -> Unit) {
    DlgEditFile(viewModel).editFile(nota, insert)
  }

  override fun editRmk(nota: NotaEntrada, save: (NotaEntrada) -> Unit) {
    DlgEditRmk().editRmk(nota, save)
  }

  override fun editRmkVend(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    DlgEditRmkVend().editRmk(fornecedor, save)
  }

  override fun imprimeSelecionados(notas: List<NotaEntrada>) {
    val report = RelatorioNotaDevolucao.processaRelatorio(notas)
    val chave = "CtlReport"
    SubWindowPDF(chave, report).open()
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
      viewModel.editRmkVend(fornecedor)
    }
    fornecedorUltimaData()
    fornecedorCodigo()
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
      addColumnButton(FILE_PICTURE, "Arquivos", "Arq", ::configIconArq) { nota ->
        viewModel.editFile(nota)
      }
      addColumnButton(EDIT, "Editor", "Edt", ::configIconEdt) { nota ->
        viewModel.editRmk(nota)
      }
      addColumnButton(ENVELOPE_O, "Editor", "Email", ::configMostraEmail) { nota ->
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

  private fun configIconEdt(icon: Icon, nota: NotaEntrada) {
    if (nota.rmk.isNotBlank()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configMostraEmail(icon: Icon, nota: NotaEntrada) {
    if (nota.listEmailNota().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }

  private fun configIconArq(icon: Icon, nota: NotaEntrada) {
    if (nota.listFiles().isNotEmpty()) icon.color = "DarkGreen"
    else icon.color = ""
  }
}

class DlgSelecionaEmail(val viewModel: TabFornecedorViewModel) {
  fun selecionaEmail(nota: NotaEntrada, emails: List<EmailDB>) {
    val form = SubWindowForm("NF: ${nota.nota}|FORNECEDOR: ${nota.vendno}") {
      createGridEmail(nota, emails)
    }
    form.open()
  }

  private fun createGridEmail(nota: NotaEntrada, emails: List<EmailDB>): Grid<EmailDB> {
    val gridDetail = Grid(EmailDB::class.java, false)
    val lista = emails + nota.listaEmailRecebidoNota()
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false // setSelectionMode(MULTI)
      setItems(lista.sortedWith(compareByDescending<EmailDB> { it.data }.thenByDescending { it.hora }))

      addColumnButton(EDIT, "Edita e-mail", "Edt") { emailEnviado ->
        editEmail(nota, emailEnviado)
      }

      emailData()
      emailHora()
      emailAssunto()
      emailTipo()
      emailEmail()
    }
  }

  private fun editEmail(nota: NotaEntrada, emailEnviado: EmailDB?) {
    val form = SubWindowForm("FORNECEDOR: ${nota.vendno}") {
      FormEmail(viewModel, listOf(nota), emailEnviado)
    }
    form.open()
  }
}

class FormEmail(val viewModel: IEmailView, notas: List<NotaEntrada>, emailEnviado: EmailDB? = null) : VerticalLayout() {
  private lateinit var chkPlanilha: Checkbox
  private lateinit var edtAssunto: TextField
  private var rteMessage: TextArea
  private lateinit var chkAnexos: Checkbox
  private lateinit var chkRelatorio: Checkbox
  private lateinit var cmbEmail: ComboBox<String>
  private var gmail: EmailGmail
    get() = EmailGmail(
      email = cmbEmail.value ?: "",
      assunto = edtAssunto.value ?: "",
      msg = { rteMessage.value ?: "" },
      msgHtml = rteMessage.value ?: "",
      planilha = if (chkPlanilha.value) "S" else "N",
      relatorio = if (chkRelatorio.value) "S" else "N",
      anexos = if (chkAnexos.value) "S" else "N",
      messageID = ""
                      )
    set(value) {
      cmbEmail.value = value.email
      edtAssunto.value = value.assunto
      rteMessage.value = htmlToText(value.msg()) //rteMessage.sanitizeHtml(value.msg.htmlFormat(), SanitizeType.none)
      chkPlanilha.value = value.planilha == "S"
      chkRelatorio.value = value.relatorio == "S"
      chkAnexos.value = value.anexos == "S"
    }

  init {
    val fornecedores = Fornecedor.findFornecedores(FiltroFonecedor(""))
    val fornecedor = notas.map { it.vendno }.distinct().mapNotNull { vendno ->
      fornecedores.firstOrNull { it.vendno == vendno }
    }.firstOrNull()
    rteMessage = richEditor()
    setSizeFull()
    horizontalLayout {
      setWidthFull()
      cmbEmail = comboBox("E-Mail") {
        this.width = "400px"
        this.isAllowCustomValue = true
        setItems(viewModel.listEmail(fornecedor))
        addCustomValueSetListener { event ->
          this.value = event.detail
        }
      }
      edtAssunto = textField("Assunto") {
        this.isExpand = true
      }
      chkRelatorio = checkBox("Relatório")
      chkPlanilha = checkBox("Planilha")
      chkAnexos = checkBox("Anexos")

      button("Enviar") { // val numerosNota = notas.joinToString(separator = " ") {it.nota}
        onLeftClick {
          viewModel.enviaEmail(gmail, notas)
        }
      }
    }
    rteMessage.width = "100%"

    addAndExpand(rteMessage)
    emailEnviado?.let { email ->
      gmail = email.emailBean()
    }
  }

  private fun richEditor(): TextArea {
    return TextArea()
  }
}

class DlgEditFile(val viewModel: TabFornecedorViewModel) {
  fun editFile(nota: NotaEntrada, insert: (NFFile) -> Unit) {
    val grid = createFormEditFile(nota)
    val form = SubWindowForm("NF: ${nota.nota}|FORNECEDOR: ${nota.vendno}", toolBar = { _ ->
      val (buffer, upload) = uploadFile()
      upload.addSucceededListener {
        val fileName = it.fileName
        val bytes = buffer.getInputStream(fileName).readBytes()
        val nfFile = NFFile.new(nota, fileName, bytes)
        insert(nfFile)
        grid.setItems(nota.listFiles())
      }
    }) {
      grid
    }
    form.open()
  }

  private fun createFormEditFile(nota: NotaEntrada): Grid<NFFile> {
    val gridDetail = Grid(NFFile::class.java, false)
    return gridDetail.apply {
      addThemeVariants(LUMO_COMPACT)
      isMultiSort = false
      setItems(nota.listFiles()) //
      addColumnButton(EYE, "Visualizar", "Ver") { file ->
        val form = SubWindowForm(file.nome, toolBar = { }) {
          val div = Div()
          div.showOutput(file.nome, file.file)
          div
        }
        form.open()
      }
      addColumnButton(TRASH, "Remover arquivo", "Rem") { file ->
        viewModel.deleteFile(file)
        setItems(nota.listFiles())
      }
      nfFileDescricao()
      nfFileData()
    }
  }

  private fun HasComponents.uploadFile(): Pair<MultiFileMemoryBuffer, Upload> {
    val buffer = MultiFileMemoryBuffer()
    val upload = Upload(buffer)
    upload.setAcceptedFileTypes("image/jpeg", "image/png", "application/pdf", "text/plain")
    val uploadButton = Button("Arquivo Nota")
    upload.uploadButton = uploadButton
    upload.isAutoUpload = true
    upload.maxFileSize = 1024 * 1024 * 1024
    upload.addFileRejectedListener { event: FileRejectedEvent ->
      println(event.errorMessage)
    }
    upload.addFailedListener { event ->
      println(event.reason.message)
    }
    add(upload)
    return Pair(buffer, upload)
  }
}

class DlgEditRmk {
  fun editRmk(nota: NotaEntrada, save: (NotaEntrada) -> Unit) {
    val form = SubWindowForm("NF: ${nota.nota}|FORNECEDOR: ${nota.vendno}", toolBar = { window ->
      button("Salva") {
        icon = CHECK.create()
        onLeftClick {
          save(nota)
          window.close()
        }
      }
    }) {
      createFormEditRmk(nota)
    }
    form.open()
  }

  private fun createFormEditRmk(nota: NotaEntrada): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = nota.rmk
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        val text = it.value
        nota.rmk = text
      }
    }
  }
}

class DlgEditRmkVend {
  fun editRmk(fornecedor: Fornecedor, save: (Fornecedor) -> Unit) {
    val form = SubWindowForm("FORNECEDOR: ${fornecedor.vendno}", toolBar = { window ->
      button("Salva") {
        icon = CHECK.create()
        onLeftClick {
          save(fornecedor)
          window.close()
        }
      }
    }) {
      createFormEditRmk(fornecedor)
    }
    form.open()
  }

  private fun createFormEditRmk(fornecedor: Fornecedor): Component {
    return TextArea().apply {
      this.style.set("overflow-y", "auto")
      this.isExpand = true
      this.focus()
      this.value = fornecedor.rmkVend
      valueChangeMode = TIMEOUT
      addValueChangeListener {
        val text = it.value
        fornecedor.rmkVend = text
      }
    }
  }
}