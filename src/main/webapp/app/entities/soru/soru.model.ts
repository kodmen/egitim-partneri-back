import { ISoruKazanimlari } from 'app/entities/soru-kazanimlari/soru-kazanimlari.model';
import { ISoruTest } from 'app/entities/soru-test/soru-test.model';

export interface ISoru {
  id?: number;
  cevap?: string | null;
  kazanimlars?: ISoruKazanimlari[] | null;
  aitOldTestlers?: ISoruTest[] | null;
}

export class Soru implements ISoru {
  constructor(
    public id?: number,
    public cevap?: string | null,
    public kazanimlars?: ISoruKazanimlari[] | null,
    public aitOldTestlers?: ISoruTest[] | null
  ) {}
}

export function getSoruIdentifier(soru: ISoru): number | undefined {
  return soru.id;
}
