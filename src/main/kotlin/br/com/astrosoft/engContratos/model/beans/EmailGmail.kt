package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci

data class EmailGmail(val messageID: String,
                      val email: String,
                      val assunto: String,
                      val msg: () -> String,
                      val msgHtml: String,
                      val planilha: String,
                      val relatorio: String,
                      val relatorioResumido: String,
                      val anexos: String) {
  fun salvaEmail(idEmail: Int) {
    saci.salvaEmailEnviado(this, idEmail)
  }
}
