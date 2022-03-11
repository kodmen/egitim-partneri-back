import { ISoru } from 'app/entities/soru/soru.model';
import { IBolum } from 'app/entities/bolum/bolum.model';

export interface ISoruTest {
  id?: number;
  tesBaslik?: string | null;
  testPdf?: string | null;
  testFotoContentType?: string | null;
  testFoto?: string | null;
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
    public sorulars?: ISoru[] | null,
    public testBolum?: IBolum | null
  ) {}
}

export function getSoruTestIdentifier(soruTest: ISoruTest): number | undefined {
  return soruTest.id;
}
