import dayjs from 'dayjs/esm';
import { IDersAnaliz } from 'app/entities/ders-analiz/ders-analiz.model';
import { IDers } from 'app/entities/ders/ders.model';
import { IOgrenci } from 'app/entities/ogrenci/ogrenci.model';

export interface IKayit {
  id?: number;
  puan?: number | null;
  kayitTarih?: dayjs.Dayjs | null;
  dersAnalizleris?: IDersAnaliz[] | null;
  aitOldDers?: IDers | null;
  kayitOgrenci?: IOgrenci | null;
}

export class Kayit implements IKayit {
  constructor(
    public id?: number,
    public puan?: number | null,
    public kayitTarih?: dayjs.Dayjs | null,
    public dersAnalizleris?: IDersAnaliz[] | null,
    public aitOldDers?: IDers | null,
    public kayitOgrenci?: IOgrenci | null
  ) {}
}

export function getKayitIdentifier(kayit: IKayit): number | undefined {
  return kayit.id;
}
