import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DersAnalizComponent } from './list/ders-analiz.component';
import { DersAnalizDetailComponent } from './detail/ders-analiz-detail.component';
import { DersAnalizUpdateComponent } from './update/ders-analiz-update.component';
import { DersAnalizDeleteDialogComponent } from './delete/ders-analiz-delete-dialog.component';
import { DersAnalizRoutingModule } from './route/ders-analiz-routing.module';

@NgModule({
  imports: [SharedModule, DersAnalizRoutingModule],
  declarations: [DersAnalizComponent, DersAnalizDetailComponent, DersAnalizUpdateComponent, DersAnalizDeleteDialogComponent],
  entryComponents: [DersAnalizDeleteDialogComponent],
})
export class DersAnalizModule {}
