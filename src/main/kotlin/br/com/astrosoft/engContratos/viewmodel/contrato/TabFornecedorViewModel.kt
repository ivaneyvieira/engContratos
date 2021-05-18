package br.com.astrosoft.engContratos.viewmodel.contrato

import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.engContratos.model.planilhas.PlanilhaNotas
import br.com.astrosoft.engContratos.view.reports.RelatorioNotaDevolucao
import br.com.astrosoft.framework.model.FileAttach
import br.com.astrosoft.framework.model.MailGMail
import br.com.astrosoft.framework.viewmodel.ITabView
import br.com.astrosoft.framework.viewmodel.IViewModelUpdate
import br.com.astrosoft.framework.viewmodel.fail

class TabFornecedorViewModel(val viewModel: ContratoViewModel):IEmailView {
  val subView
    get() = viewModel.view.tabFornecedor

  override fun updateView() = viewModel.exec {
    subView.updateGrid(listFornecedores())
  }

  fun listFornecedores(): List<Fornecedor> {
    val filtro = subView.filtro()
    return Fornecedor.findFornecedores(filtro)
  }

  fun imprimirNotaContrato(notas: List<NotaEntrada>) {
    notas.ifEmpty {
      fail("Não nenhuma nota selecionada")
    }
    subView.imprimeSelecionados(notas)
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

  override fun listEmail(fornecedor: Fornecedor?): List<String> {
    return fornecedor?.listEmail().orEmpty()
  }

  private fun createReports(gmail: EmailGmail, notas: List<NotaEntrada>): List<FileAttach> {
    val relatoriosCompleto = when (gmail.relatorio) {
      "S"  -> {
        notas.map { nota ->
          val report = RelatorioNotaDevolucao.processaRelatorio(listOf(nota))
          FileAttach("Relatorio da nota ${nota.nota.replace("/", "_")}.pdf", report)
        }
      }

      else -> emptyList()
    }

    return relatoriosCompleto
  }

  private fun createPlanilha(gmail: EmailGmail, notas: List<NotaEntrada>): List<FileAttach> {
    return when (gmail.planilha) {
      "S"  -> {
        notas.map { nota ->
          val planilha = geraPlanilha(listOf(nota))
          FileAttach("Planilha da Nota ${nota.nota.replace("/", "_")}.xlsx", planilha)
        }
      }

      else -> emptyList()
    }
  }

  fun geraPlanilha(notas: List<NotaEntrada>): ByteArray {
    val planilha = PlanilhaNotas()

    return planilha.grava(notas)
  }

  private fun createAnexos(gmail: EmailGmail, notas: List<NotaEntrada>): List<FileAttach> {
    return when (gmail.anexos) {
      "S"  -> {
        notas.flatMap { nota ->
          nota.listFiles().map { nfile ->
            FileAttach(nfile.nome, nfile.file)
          }
        }
      }

      else -> emptyList()
    }
  }

  override fun enviaEmail(gmail: EmailGmail, notas: List<NotaEntrada>) = viewModel.exec {
    val mail = MailGMail()
    val filesReport = createReports(gmail, notas)
    val filesPlanilha = createPlanilha(gmail, notas)
    val filesAnexo = createAnexos(gmail, notas)
    val enviadoComSucesso = mail.sendMail(gmail.email,
                                          gmail.assunto,
                                          gmail.msgHtml,
                                          filesReport + filesPlanilha + filesAnexo)
    if (enviadoComSucesso) {
      val idEmail = EmailDB.newEmailId()
      notas.forEach { nota ->
        nota.salvaEmail(gmail, idEmail)
      }
    }
    else fail("Erro ao enviar e-mail")
  }

  fun deleteFile(file: NFFile?) = viewModel.exec {
    file?.apply {
      this.delete()
    }
  }

  fun editRmkVend(fornecedor: Fornecedor) {
    subView.editRmkVend(fornecedor) { forn ->
      forn.saveRmkVend()
    }
  }
}

interface ITabFornecedor : ITabView {
  fun filtro(): FiltroFonecedor
  fun updateGrid(itens: List<Fornecedor>)
  fun selecionaEmail(nota: NotaEntrada, emails: List<EmailDB>)
  fun editFile(nota: NotaEntrada, insert: (NFFile) -> Unit)
  fun editRmk(nota: NotaEntrada, save: (NotaEntrada) -> Unit)
  fun editRmkVend(fornecedor: Fornecedor, save: (Fornecedor) -> Unit)
  fun imprimeSelecionados(notas: List<NotaEntrada>)
}

interface IEmailView : IViewModelUpdate {
  fun listEmail(fornecedor: Fornecedor?): List<String>
  fun enviaEmail(gmail: EmailGmail, notas: List<NotaEntrada>)
}