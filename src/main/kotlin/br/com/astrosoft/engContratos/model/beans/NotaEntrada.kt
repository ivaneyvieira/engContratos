package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci
import java.time.LocalDate

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
  val rmk: String,
                 ){

  fun listEmailNota() = saci.listEmailNota(this)

  fun listFiles() = saci.selectFile(this)

  fun saveRmk() = saci.saveRmk(this)
}