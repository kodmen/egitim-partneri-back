import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OgrenciComponent } from './list/ogrenci.component';
import { OgrenciDetailComponent } from './detail/ogrenci-detail.component';
import { OgrenciUpdateComponent } from './update/ogrenci-update.component';
import { OgrenciDeleteDialogComponent } from './delete/ogrenci-delete-dialog.component';
import { OgrenciRoutingModule } from './route/ogrenci-routing.module';

@NgModule({
  imports: [SharedModule, OgrenciRoutingModule],
  declarations: [OgrenciComponent, OgrenciDetailComponent, OgrenciUpdateComponent, OgrenciDeleteDialogComponent],
  entryComponents: [OgrenciDeleteDialogComponent],
})
export class OgrenciModule {}
