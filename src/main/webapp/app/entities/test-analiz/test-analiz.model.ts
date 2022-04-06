import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';

export interface ITestAnaliz {
  id?: number;
  dogru?: number | null;
  yanlis?: number | null;
  bos?: number | null;
  net?: number | null;
  tamamlandi?: boolean | null;
  testId?: number | null;
  dersAnaliz?: IDersAnaliz | null;
}

export class TestAnaliz implements ITestAnaliz {
  constructor(
    public id?: number,
    public dogru?: number | null,
    public yanlis?: number | null,
    public bos?: number | null,
    public net?: number | null,
    public tamamlandi?: boolean | null,
    public testId?: number | null,
    public dersAnaliz?: IDersAnaliz | null
  ) {
    this.tamamlandi = this.tamamlandi ?? false;
  }
}

export function getTestAnalizIdentifier(testAnaliz: ITestAnaliz): number | undefined {
  return testAnaliz.id;
}
