import { ISoru } from 'app/entities/soru/soru.model';

export interface ISoruKazanimlari {
  id?: number;
  kazanim?: string | null;
  aitOldSorulars?: ISoru[] | null;
}

export class SoruKazanimlari implements ISoruKazanimlari {
  constructor(public id?: number, public kazanim?: string | null, public aitOldSorulars?: ISoru[] | null) {}
}

export function getSoruKazanimlariIdentifier(soruKazanimlari: ISoruKazanimlari): number | undefined {
  return soruKazanimlari.id;
}
