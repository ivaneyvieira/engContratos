package br.com.astrosoft.engContratos.model

import br.com.astrosoft.AppConfig
import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB
import br.com.astrosoft.framework.util.toSaciDate

class QuerySaci : QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
      addParameter("appName", AppConfig.appName)
    }.firstOrNull()
  }

  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
      addParameter("appName", AppConfig.appName)
    }
  }

  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso)
      addOptionalParameter("loja", user.storeno)
      addOptionalParameter("appName", AppConfig.appName)
    }
  }

  fun allLojas(): List<Loja> {
    val sql = "/sqlSaci/listLojas.sql"
    return query(sql, Loja::class)
  }

  fun listFornecedores(filtro: FiltroFonecedor): List<Fornecedor> {
    val sql = "/sqlSaci/listFornecedores.sql"
    return query(sql, Fornecedor::class) {
      addOptionalParameter("localizador", filtro.localizador)
    }
  }

  fun listNotaFornecedor(fornecedor: Fornecedor): List<NotaEntrada> {
    val sql = "/sqlSaci/listNotaFornecedor.sql"
    return query(sql, NotaEntrada::class) {
      addOptionalParameter("vendno", fornecedor.vendno)
    }
  }

  fun saveRmk(nota: NotaEntrada) {
    val sql = "/sqlSaci/rmkUpdate.sql"

    script(sql) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nota.ni)
      addOptionalParameter("rmk", nota.rmk)
    }
  }

  fun listEmailNota(nota: NotaEntrada): List<EmailDB> {
    val sql = "/sqlSaci/listEmailEnviado.sql"
    return query(sql, EmailDB::class) {
      addOptionalParameter("storeno", nota.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nota.ni)
    }
  }

  fun newEmailId(): Int {
    val sql = "select MAX(idEmail + 1) as max from sqldados.devEmail"
    return query(sql, Max::class).firstOrNull()?.max ?: 1
  }

  fun listEmailPara(): List<EmailDB> {
    val sql = "/sqlSaci/listEmailEnviadoPara.sql" //return emptyList()
    return query(sql, EmailDB::class)
  }

  fun salvaEmailEnviado(gmail: EmailGmail, nota: NotaEntrada, idEmail: Int) {
    val sql = "/sqlSaci/salvaEmailEnviado.sql"
    val storeno = nota.loja
    val pdvno = 9999
    val xano = nota.ni
    script(sql) {
      addOptionalParameter("idEmail", idEmail)
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("pdvno", pdvno)
      addOptionalParameter("xano", xano)
      addOptionalParameter("email", gmail.email)
      addOptionalParameter("messageID", gmail.messageID)
      addOptionalParameter("assunto", gmail.assunto)
      addOptionalParameter("msg", gmail.msg())
      addOptionalParameter("planilha", gmail.planilha)
      addOptionalParameter("relatorio", gmail.relatorio)
      addOptionalParameter("anexos", gmail.anexos)
    }
  }

  fun salvaEmailEnviado(gmail: EmailGmail, idEmail: Int) {
    val sql = "/sqlSaci/salvaEmailEnviado.sql"
    val storeno = 0
    val pdvno = 0
    val xano = 0
    script(sql) {
      addOptionalParameter("idEmail", idEmail)
      addOptionalParameter("storeno", storeno)
      addOptionalParameter("pdvno", pdvno)
      addOptionalParameter("xano", xano)
      addOptionalParameter("email", gmail.email)
      addOptionalParameter("assunto", gmail.assunto)
      addOptionalParameter("messageID", gmail.messageID)
      addOptionalParameter("msg", gmail.msg())
      addOptionalParameter("planilha", gmail.planilha)
      addOptionalParameter("relatorio", gmail.relatorio)
      addOptionalParameter("anexos", gmail.anexos)
    }
  }

  fun listNotasEmailNota(idEmail: Int): List<EmailDB> {
    val sql = "/sqlSaci/listNotasEmailEnviado.sql"
    return query(sql, EmailDB::class) {
      addOptionalParameter("idEmail", idEmail)
    }
  }

  //Files
  fun insertFile(file: NFFile) {
    val sql = "/sqlSaci/fileInsert.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("file", file.file)
    }
  }

  fun updateFile(file: NFFile) {
    val sql = "/sqlSaci/fileUpdate.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
      addOptionalParameter("file", file.file)
    }
  }

  fun deleteFile(file: NFFile) {
    val sql = "/sqlSaci/fileDelete.sql"
    script(sql) {
      addOptionalParameter("storeno", file.storeno)
      addOptionalParameter("pdvno", file.pdvno)
      addOptionalParameter("xano", file.xano)
      addOptionalParameter("date", file.date.toSaciDate())
      addOptionalParameter("nome", file.nome)
    }
  }

  fun selectFile(nfe: NotaEntrada): List<NFFile> {
    val sql = "/sqlSaci/fileSelect.sql"
    return query(sql, NFFile::class) {
      addOptionalParameter("storeno", nfe.loja)
      addOptionalParameter("pdvno", 9999)
      addOptionalParameter("xano", nfe.ni)
    }
  }

  fun saveRmkVend(fornecedor: Fornecedor) {
    val sql = "/sqlSaci/rmkUpdateVend.sql"
    script(sql) {
      addOptionalParameter("vendno", fornecedor.vendno)
      addOptionalParameter("tipo", "99")
      addOptionalParameter("rmk", fornecedor.rmkVend)
    }
  }

  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    val ipServer: String? = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()

data class Max(val max: Int)