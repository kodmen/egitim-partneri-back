import { ISoru } from 'app/entities/soru/soru.model';
import { IBolum } from 'app/entities/bolum/bolum.model';

export interface ISoruTest {
  id?: number;
  tesBaslik?: string | null;
  testPdf?: string | null;
  testFotoContentType?: string | null;
  testFoto?: string | null;
  cevaplar?: string | null;
  soruPdfFileContentType?: string | null;
  soruPdfFile?: string | null;
  soruSayisi?: number | null;
  seviye?: string | null;
  sorulars?: ISoru[] | null;
  testBolum?: IBolum | null;
}

export class SoruTest implements ISoruTest {
  constructor(
    public id?: number,
    public tesBaslik?: string | null,
    public testPdf?: string | null,
    public testFotoContentType?: string | null,
    public testFoto?: string | null,
    public cevaplar?: string | null,
    public soruPdfFileContentType?: string | null,
    public soruPdfFile?: string | null,
    public soruSayisi?: number | null,
    public seviye?: string | null,
    public sorulars?: ISoru[] | null,
    public testBolum?: IBolum | null
  ) {}
}

export function getSoruTestIdentifier(soruTest: ISoruTest): number | undefined {
  return soruTest.id;
}
