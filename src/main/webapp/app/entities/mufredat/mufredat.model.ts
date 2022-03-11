import { IBolum } from 'app/entities/bolum/bolum.model';
import { IDers } from 'app/entities/ders/ders.model';

export interface IMufredat {
  id?: number;
  mufredatBaslik?: string | null;
  toplamSure?: string | null;
  bolumSayi?: number | null;
  bolumlers?: IBolum[] | null;
  mufredatDers?: IDers | null;
}

export class Mufredat implements IMufredat {
  constructor(
    public id?: number,
    public mufredatBaslik?: string | null,
    public toplamSure?: string | null,
    public bolumSayi?: number | null,
    public bolumlers?: IBolum[] | null,
    public mufredatDers?: IDers | null
  ) {}
}

export function getMufredatIdentifier(mufredat: IMufredat): number | undefined {
  return mufredat.id;
}
