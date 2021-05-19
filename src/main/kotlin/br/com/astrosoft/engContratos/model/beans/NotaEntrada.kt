package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci
import br.com.astrosoft.framework.model.EmailMessage
import br.com.astrosoft.framework.model.GamilFolder
import br.com.astrosoft.framework.model.MailGMail
import java.time.LocalDate
import javax.mail.internet.InternetAddress

class NotaEntrada(
  val loja: Int,
  val sigla: String,
  val ni: Int,
  val nota: String,
  val fatura: String,
  val dataNota: LocalDate,
  val vendno: Int,
  val valor: Double,
  val obsNota: String,
  val remarks: String,
  var rmk: String,
                 ) {

  fun listEmailNota() = saci.listEmailNota(this)

  fun listFiles() = saci.selectFile(this)

  fun saveRmk() = saci.saveRmk(this)

  fun listaEmailRecebidoNota(): List<EmailDB> {
    val gmail = MailGMail()
    val numero = nota.split("/")[0]
    return gmail.listEmail(GamilFolder.Todos, numero).map { msg: EmailMessage ->
      EmailDB(
        storeno = loja,
        pdvno = 9999,
        xano = ni,
        data = msg.data.toLocalDate(),
        hora = msg.data.toLocalTime(),
        idEmail = 0,
        messageID = msg.messageID,
        email = (msg.from.getOrNull(0) as? InternetAddress)?.address ?: "",
        assunto = msg.subject,
        msg = msg.content().messageTxt,
        planilha = "N",
        relatorio = "N",
        anexos = "N",
             )
    }
  }

  fun listaProdutos() = saci.produtosEntrada(this)

  fun salvaEmail(gmail: EmailGmail, idEmail: Int) {
    saci.salvaEmailEnviado(gmail, this, idEmail)
  }
}