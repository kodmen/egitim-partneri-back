import { IOgrenci } from 'app/entities/ogrenci/ogrenci.model';

export interface IRozet {
  id?: number;
  rozetIsmi?: string | null;
  rozetResimContentType?: string | null;
  rozetResim?: string | null;
  ogrencilers?: IOgrenci[] | null;
}

export class Rozet implements IRozet {
  constructor(
    public id?: number,
    public rozetIsmi?: string | null,
    public rozetResimContentType?: string | null,
    public rozetResim?: string | null,
    public ogrencilers?: IOgrenci[] | null
  ) {}
}

export function getRozetIdentifier(rozet: IRozet): number | undefined {
  return rozet.id;
}
