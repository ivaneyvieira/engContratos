package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci
import java.time.LocalDate

class NFFile(
  val storeno: Int,
  val pdvno: Int,
  val xano: Int,
  val date: LocalDate,
  var nome: String,
  var file: ByteArray,
            ) {
  fun insert() = saci.insertFile(this)

  fun update() = saci.updateFile(this)

  fun delete() = saci.deleteFile(this)

  fun saveFile() {
    saci.insertFile(this)
  }

  companion object {
    fun new(nota: NotaEntrada, fileName: String, bytes: ByteArray): NFFile {
      return NFFile(
        storeno = nota.loja, pdvno = 9999, xano = nota.ni, date = LocalDate.now(), nome = fileName, file = bytes
                   )
    }
  }
}

