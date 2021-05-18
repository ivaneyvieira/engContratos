package br.com.astrosoft.engContratos.model.planilhas

import br.com.astrosoft.engContratos.model.beans.NotaEntrada
import br.com.astrosoft.engContratos.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.model.Campo
import br.com.astrosoft.framework.model.CampoInt
import br.com.astrosoft.framework.model.CampoNumber
import br.com.astrosoft.framework.model.CampoString
import org.apache.poi.ss.usermodel.FillPatternType
import com.github.nwillc.poink.workbook
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import java.io.ByteArrayOutputStream
import br.com.astrosoft.framework.util.format

class PlanilhaNotas {
  private val campos: List<Campo<*, ProdutosNotaSaida>> = listOf(
    CampoString("Rótulo") { rotulo },
    CampoInt("Fornecedor") { vendno },
    CampoInt("NI") { invno },
    CampoString("NF") { notaInv },
    CampoString("Emissão") { dateInv?.format() ?: "" },
    CampoInt("Qnt NI") { quantInv },
    CampoInt("Qnt Dev") { qtde },
    CampoString("Código") { codigo },
    CampoString("Descrição") { descricao },
    CampoString("Grade") { grade },
    CampoString("Ref Forn") { refFor },
    CampoNumber("R$ Unit") { valorUnitario },
    CampoNumber("R$ IPI") { ipi },
    CampoNumber("R$ ST") { vst },
    CampoNumber("R$ Total") { valorTotalIpi },
    CampoString("Chave") {
      val text = chaveUlt ?: ""
      text.substring(0, 6.coerceAtMost(text.length))
    },
                                                                )

  fun grava(listaNotas: List<NotaEntrada>): ByteArray {
    val wb = workbook {
      val headerStyle = cellStyle("Header") {
        fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        fillPattern = FillPatternType.SOLID_FOREGROUND
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val rowStyle = cellStyle("Row") {
        this.verticalAlignment = VerticalAlignment.TOP
      }
      val stNotas = sheet("Produtos") {
        val headers = campos.map { it.header }
        row(headers, headerStyle)
        listaNotas.flatMap { it.listaProdutos() }.sortedBy { it.loja }.forEach { produto ->
          val valores = campos.map { it.produceVakue(produto) }
          row(valores, rowStyle)
        }
      }

      campos.forEachIndexed { index, _ ->
        stNotas.autoSizeColumn(index)
      }
    }
    val outBytes = ByteArrayOutputStream()
    wb.write(outBytes)
    return outBytes.toByteArray()
  }
}